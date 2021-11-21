package com.tinkoff.coursework.presentation.activity.chat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ActivityChatBinding
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.presentation.adapter.paginator.PaginatorRecyclerView
import com.tinkoff.coursework.presentation.adapter.viewtype.DateDividerViewType
import com.tinkoff.coursework.presentation.adapter.viewtype.IncomingMessageViewType
import com.tinkoff.coursework.presentation.adapter.viewtype.OutcomingMessageViewType
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.dialog.emoji.BottomSheetDialogWithReactions
import com.tinkoff.coursework.presentation.model.EmojiUI
import com.tinkoff.coursework.presentation.model.StreamUI
import com.tinkoff.coursework.presentation.model.TopicUI
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import vivid.money.elmslie.android.base.ElmActivity
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : ElmActivity<ChatEvent, ChatAction, ChatUIState>(), BottomSheetDialogWithReactions.OnEmojiPickListener {

    companion object {
        private const val EXTRA_STREAM = "EXTRA_STREAM"
        private const val EXTRA_TOPIC = "EXTRA_TOPIC"
        private const val PAGINATION_OFFSET = 20

        fun getIntent(context: Context, stream: StreamUI, topic: TopicUI): Intent {
            val intentToChatActivity = Intent(context, ChatActivity::class.java)
            intentToChatActivity.putExtra(EXTRA_STREAM, stream)
            intentToChatActivity.putExtra(EXTRA_TOPIC, topic)
            return intentToChatActivity
        }
    }

    @Inject
    lateinit var chatActor: ChatActor

    override val initEvent: ChatEvent
        get() = ChatEvent.Ui.InitEvent

    private val filePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            contentResolver.openInputStream(it)?.let { inpStr ->
                store.accept(ChatEvent.Ui.UploadFile(inpStr))
            }
        }
    }

    private val paginator = PaginatorRecyclerView(
        loadMoreItems = {
            store.accept(
                ChatEvent.Ui.LoadMessages
            )
        }
    )

    private val chatAdapter = DelegateAdapter(getSupportedViewTypesForChatRv())

    private val dialogWithReactions =
        BottomSheetDialogWithReactions.newInstance()

    private lateinit var binding: ActivityChatBinding

    private val currentStream: StreamUI
        get() = intent.getParcelableExtra(EXTRA_STREAM) ?: throw IllegalStateException()

    private val currentTopic: TopicUI
        get() = intent.getParcelableExtra(EXTRA_TOPIC) ?: throw IllegalStateException()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initActionBar()
        initRecyclerView()
        setTextWatcher()
        setListener()
    }

    override fun onPause() {
        super.onPause()
        binding.chatShimmer.stopShimmer()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onEmojiPicked(emoji: EmojiUI) {
        store.accept(
            ChatEvent.Ui.EmojiPicked(
                emoji
            )
        )
    }

    override fun createStore(): Store<ChatEvent, ChatAction, ChatUIState> {
        return ElmStoreCompat(
            initialState = ChatUIState(
                currentStream = currentStream,
                currentTopic = currentTopic,
                olderMessageId = -1,
                paginationOffset = PAGINATION_OFFSET
            ),
            reducer = ChatReducer(),
            actor = chatActor
        )
    }

    override fun render(state: ChatUIState) {
        state.apply {
            chatEntities?.let {
                chatAdapter.setItems(it)
            }
            paginator.isLoading = loadingNewMessages == LoadingState.LOADING
            binding.rvMessages.isInvisible = loadingState == LoadingState.LOADING
            binding.chatShimmer.isVisible = loadingState == LoadingState.LOADING
            if (state.loadingState == LoadingState.LOADING) binding.chatShimmer.startShimmer()
            else binding.chatShimmer.stopShimmer()
            binding.chatBtnAction.isGone = loadingInput == LoadingState.LOADING
            binding.chatInputLoadingIndicator.isGone = binding.chatBtnAction.isGone.not()
        }
    }

    override fun handleEffect(effect: ChatAction): Unit = when (effect) {
        is ChatAction.OpenBottomSheetDialog ->
            dialogWithReactions.show(supportFragmentManager, null)
        is ChatAction.HideBottomSheetDialog -> dialogWithReactions.dismiss()
        is ChatAction.ShowToastMessage ->
            showToast(effect.message)
        is ChatAction.ShowPreviouslyTypedMessage ->
            binding.chatInput.text = SpannableStringBuilder(effect.message)
        is ChatAction.DisablePagination ->
            binding.rvMessages.removeOnScrollListener(paginator)
        ChatAction.EnablePagination -> setPaginator()
        is ChatAction.OpenUriInBrowser -> {
            startActivityForBrowsingUri(effect.uri)
        }
        ChatAction.OpenFilePicker -> {
            filePicker.launch("image/*")
        }
        is ChatAction.ShowFileUrlWithName -> {
            binding.chatInput.text = SpannableStringBuilder(String.format(
                getString(R.string.file_uploaded_template),
                binding.chatInput.text.toString(),
                effect.name,
                effect.uri.toString()
            ))
        }
    }

    private fun initActionBar() {
        setSupportActionBar(binding.chatToolBar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            title = currentStream.name
        }
        binding.chatTopic.text = String.format(
            resources.getString(R.string.chat_topic),
            currentTopic.name
        )
    }

    private fun initRecyclerView() {
        binding.rvMessages.adapter = chatAdapter
        binding.rvMessages.addItemDecoration(
            OffsetItemDecorator(
                top = resources.getDimensionPixelSize(R.dimen.chat_items_margin_top),
                bottom = resources.getDimensionPixelSize(R.dimen.chat_items_margin_bottom)
            )
        )
    }

    private fun setTextWatcher() {
        binding.chatInput.doOnTextChanged { text, _, _, _ ->
            val iconForShowing =
                if (text?.isEmpty() == true) R.drawable.ic_add_files
                else R.drawable.ic_send_message
            binding.chatBtnAction.setImageResource(iconForShowing)
        }
    }

    private fun setListener() {
        binding.chatBtnAction.setOnClickListener {
            if (binding.chatInput.text.isNotEmpty()) {
                val input = binding.chatInput.text
                store.accept(
                    ChatEvent.Ui.SendMessage(input.toString())
                )
                binding.chatInput.text = SpannableStringBuilder("")
            } else {
                store.accept(
                    ChatEvent.Ui.CallFilePicker
                )
            }
        }
    }

    private fun setPaginator() {
        binding.rvMessages.addOnScrollListener(paginator)
    }

    private fun getSupportedViewTypesForChatRv() = listOf(
        OutcomingMessageViewType(
            onMessageLongClick = { message ->
                store.accept(
                    ChatEvent.Ui.CallEmojiPicker(message)
                )
            },
            onEmojiClick = { message, reactionInContainerPosition ->
                store.accept(
                    ChatEvent.Ui.EmojiClick(
                        message,
                        reactionInContainerPosition
                    )
                )
            },
            onClickableTextClick = { messageHyperlink ->
                store.accept(ChatEvent.Ui.ClickableTextAtMessageClick(messageHyperlink))
            }
        ),
        IncomingMessageViewType(
            onMessageLongClick = { message ->
                store.accept(
                    ChatEvent.Ui.CallEmojiPicker(message)
                )
            },
            onEmojiClick = { message, reactionInContainerPosition ->
                store.accept(
                    ChatEvent.Ui.EmojiClick(
                        message,
                        reactionInContainerPosition
                    )
                )
            },
            onClickableTextClick = { messageHyperlink ->
                store.accept(ChatEvent.Ui.ClickableTextAtMessageClick(messageHyperlink))
            }
        ),
        DateDividerViewType()
    )

    private fun startActivityForBrowsingUri(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(Intent.createChooser(intent, getString(R.string.uri_browsing_chooser)))
    }
}
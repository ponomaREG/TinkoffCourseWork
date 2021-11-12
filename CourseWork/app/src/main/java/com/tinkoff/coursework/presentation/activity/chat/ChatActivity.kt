package com.tinkoff.coursework.presentation.activity.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.tinkoff.coursework.presentation.assisted_factory.ChatActivityAssistedFactory
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.dialog.emoji.BottomSheetDialogWithReactions
import com.tinkoff.coursework.presentation.model.EmojiUI
import com.tinkoff.coursework.presentation.model.StreamUI
import com.tinkoff.coursework.presentation.model.TopicUI
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity(), BottomSheetDialogWithReactions.OnEmojiPickListener {

    companion object {
        private const val EXTRA_STREAM = "EXTRA_STREAM"
        private const val EXTRA_TOPIC = "EXTRA_TOPIC"

        fun getIntent(context: Context, stream: StreamUI, topic: TopicUI): Intent {
            val intentToChatActivity = Intent(context, ChatActivity::class.java)
            intentToChatActivity.putExtra(EXTRA_STREAM, stream)
            intentToChatActivity.putExtra(EXTRA_TOPIC, topic)
            return intentToChatActivity
        }
    }

    @Inject
    lateinit var assistedFactory: ChatActivityAssistedFactory

    private val viewModel: ChatViewModel by viewModels {
        assistedFactory.also {
            it.stream = currentStream
            it.topic = currentTopic
        }
    }

    private val paginator = PaginatorRecyclerView(
        loadMoreItems = {
            viewModel.loadMessages(
                currentStream,
                currentTopic
            )
        }
    )

    private val chatAdapter = DelegateAdapter(getSupportedViewTypesForChatRv())

    private val dialogWithReactions =
        BottomSheetDialogWithReactions.newInstance()

    private lateinit var binding: ActivityChatBinding

    private val compositeDisposable = CompositeDisposable()

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
        setPaginator()
    }

    override fun onPause() {
        super.onPause()
        binding.chatShimmer.stopShimmer()
    }

    override fun onStart() {
        super.onStart()
        observeActions()
        observerState()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onEmojiPicked(emoji: EmojiUI) {
        viewModel.onEmojiAtBottomSheetDialogPicked(emoji)
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
            val input = binding.chatInput.text
            if (input.isNullOrEmpty().not()) {
                viewModel.sendMessage(currentTopic, input.toString())
                binding.chatInput.text = SpannableStringBuilder("")
            }
        }
    }

    private fun setPaginator() {
        binding.rvMessages.addOnScrollListener(paginator)
    }

    private fun getSupportedViewTypesForChatRv() = listOf(
        OutcomingMessageViewType(
            onMessageLongClick = { messagePosition ->
                viewModel.onMessageLongClick(messagePosition)
            },
            onEmojiClick = { message, adapterPosition, reactionInContainerPosition ->
                viewModel.onEmojiClick(message, adapterPosition, reactionInContainerPosition)
            },
        ),
        IncomingMessageViewType(
            onMessageLongClick = { messagePosition ->
                viewModel.onMessageLongClick(messagePosition)
            },
            onEmojiClick = { message, adapterPosition, reactionInContainerPosition ->
                viewModel.onEmojiClick(message, adapterPosition, reactionInContainerPosition)
            },
        ),
        DateDividerViewType()
    )

    private fun observerState() {
        viewModel.observableState
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                state.apply {
                    messages?.let {
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
            }.addTo(compositeDisposable)
    }

    private fun observeActions() {
        compositeDisposable.add(
            viewModel.observableAction
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { action ->
                    action?.let {
                        when (it) {
                            is ChatAction.OpenBottomSheetDialog ->
                                dialogWithReactions.show(supportFragmentManager, null)
                            is ChatAction.HideBottomSheetDialog ->
                                if (dialogWithReactions.isAdded) dialogWithReactions.dismiss()
                            is ChatAction.ShowToastMessage ->
                                showToast(it.message)
                            is ChatAction.ShowPreviouslyTypedMessage ->
                                binding.chatInput.text = SpannableStringBuilder(it.message)
                            is ChatAction.DisablePagination ->
                                binding.rvMessages.removeOnScrollListener(paginator)
                        }
                    }
                }
        )
    }
}
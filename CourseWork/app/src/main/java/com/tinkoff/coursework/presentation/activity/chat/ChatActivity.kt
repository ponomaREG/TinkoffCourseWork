package com.tinkoff.coursework.presentation.activity.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.ActivityChatBinding
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.presentation.adapter.viewtype.DateDividerViewType
import com.tinkoff.coursework.presentation.adapter.viewtype.MessageViewType
import com.tinkoff.coursework.presentation.dialog.emoji.BottomSheetDialogWithReactions
import com.tinkoff.coursework.presentation.model.Emoji
import com.tinkoff.coursework.presentation.model.Stream
import com.tinkoff.coursework.presentation.model.Topic
import com.tinkoff.coursework.presentation.util.hideKeyboard
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class ChatActivity : AppCompatActivity(), BottomSheetDialogWithReactions.OnEmojiPickListener {

    companion object {
        private const val EXTRA_STREAM = "EXTRA_STREAM"
        private const val EXTRA_TOPIC = "EXTRA_TOPIC"

        fun getIntent(context: Context, stream: Stream, topic: Topic): Intent {
            val intentToChatActivity = Intent(context, ChatActivity::class.java)
            intentToChatActivity.putExtra(EXTRA_STREAM, stream)
            intentToChatActivity.putExtra(EXTRA_TOPIC, topic)
            return intentToChatActivity
        }
    }

    private val viewModel: ChatViewModel by viewModels()

    private val chatAdapter = DelegateAdapter(getSupportedViewTypesForChatRv())

    private val dialogWithReactions =
        BottomSheetDialogWithReactions.newInstance()

    private lateinit var binding: ActivityChatBinding

    private val compositeDisposable = CompositeDisposable()

    private val currentStream: Stream
        get() = intent.getParcelableExtra(EXTRA_STREAM) ?: throw IllegalStateException()

    private val currentTopic: Topic
        get() = intent.getParcelableExtra(EXTRA_TOPIC) ?: throw IllegalStateException()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initActionBar()
        initRecyclerView()
        setTextWatcher()
        setListener()
        observerState()
        observeActions()
    }

    override fun onPause() {
        super.onPause()
        binding.chatShimmer.stopShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        viewModel.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onEmojiPicked(emoji: Emoji) {
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
                viewModel.sendMessage(input.toString())
                binding.chatInput.text = SpannableStringBuilder("")
                hideKeyboard()
            }
        }
    }

    private fun getSupportedViewTypesForChatRv() = listOf(
        MessageViewType(
            onMessageLongClick = { messagePosition ->
                viewModel.onMessageLongClick(messagePosition)
            },
            onEmojiClick = { message, adapterPosition, reactionInContainerPosition ->
                viewModel.onEmojiClick(message, adapterPosition, reactionInContainerPosition)
            }
        ),
        DateDividerViewType()
    )

    private fun observerState() {
        val disposable = viewModel.observableState
            .subscribe { state ->
                state.apply {
                    messages?.let {
                        chatAdapter.setItems(it)
                    }
                    error?.let {
                        showToast(it.message)
                    }
                    isFirstLoadingMessages?.let {
                        if (it) {
                            binding.chatShimmer.apply {
                                visibility = View.VISIBLE
                                startShimmer()
                            }
                            binding.rvMessages.visibility = View.INVISIBLE
                        } else {
                            binding.rvMessages.visibility = View.VISIBLE
                            binding.chatShimmer.apply {
                                visibility = View.GONE
                                stopShimmer()
                            }
                        }
                    }
                }
            }
        compositeDisposable.add(disposable)
    }

    private fun observeActions() {
        compositeDisposable.add(
            viewModel.observableAction.subscribe { action ->
                action?.let {
                    when (it) {
                        is ChatAction.OpenBottomSheetDialog ->
                            dialogWithReactions.show(supportFragmentManager, null)
                        is ChatAction.HideBottomSheetDialog ->
                            if (dialogWithReactions.isAdded) dialogWithReactions.dismiss()
                    }
                }
            }
        )
    }
}
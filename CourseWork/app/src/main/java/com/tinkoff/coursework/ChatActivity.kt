package com.tinkoff.coursework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.tinkoff.coursework.adapter.DelegateAdapter
import com.tinkoff.coursework.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.adapter.viewtype.DateDividerViewType
import com.tinkoff.coursework.adapter.viewtype.MessageViewType
import com.tinkoff.coursework.databinding.ActivityChatBinding
import com.tinkoff.coursework.dialog.BottomSheetDialogWithReactions
import com.tinkoff.coursework.model.*
import com.tinkoff.coursework.util.MockUtil
import com.tinkoff.coursework.util.hideKeyboard
import kotlin.random.Random

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

    private val chatAdapter = DelegateAdapter(getSupportedViewTypesForChatRv())

    private val dialogWithReactions =
        BottomSheetDialogWithReactions.newInstance()

    private lateinit var binding: ActivityChatBinding

    private var clickedMessagePosition: Int = -1

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
        chatAdapter.setItems(
            MockUtil.mockMessages()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onEmojiPicked(emoji: Emoji) {
        dialogWithReactions.dismiss()
        var message = chatAdapter.getItemAt(clickedMessagePosition)
        if (message is Message) {
            message = message.copy(reactions = message.reactions.toMutableList())
            val alreadyExistsReactionInd =
                message.reactions.indexOfFirst { it.emojiCode == emoji.emojiCode }
            if (alreadyExistsReactionInd == -1) {
                message.reactions.add(
                    Reaction(
                        emojiCode = emoji.emojiCode,
                        isSelected = true,
                        countOfVotes = 1
                    )
                )
            } else {
                val alreadyExistsReaction = message.reactions[alreadyExistsReactionInd].copy()
                if (alreadyExistsReaction.isSelected.not()) {
                    alreadyExistsReaction.apply {
                        countOfVotes += 1
                        isSelected = true
                    }
                    message.reactions[alreadyExistsReactionInd] = alreadyExistsReaction
                }
            }
            chatAdapter.replaceItemAt(clickedMessagePosition, message)
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
            if (binding.chatInput.text.isNullOrEmpty().not()) {
                sendMessage()
                binding.chatInput.text = SpannableStringBuilder("")
                hideKeyboard()
            }
        }
    }

    private fun sendMessage() {
        val textMessage = binding.chatInput.text.toString()
        chatAdapter.addItems(
            listOf(
                Message(
                    id = Random.nextInt(1, 1000),
                    username = "Пользователь",
                    message = textMessage,
                    avatarRes = R.mipmap.ic_launcher,
                    reactions = mutableListOf()
                )
            )
        )
    }

    private fun onReactionUnderMessageClick(
        message: Message,
        adapterPosition: Int,
        reactionInContainerPosition: Int
    ) {
        val messageCopy = message.copy(reactions = message.reactions.toMutableList())
        val modelReaction = messageCopy.reactions[reactionInContainerPosition].copy()
        messageCopy.reactions[reactionInContainerPosition] = modelReaction
        modelReaction.isSelected = modelReaction.isSelected.not()
        if (modelReaction.isSelected) modelReaction.countOfVotes += 1
        else {
            modelReaction.countOfVotes -= 1
            if (modelReaction.countOfVotes == 0) {
                messageCopy.reactions.removeAt(reactionInContainerPosition)
            }
        }
        chatAdapter.replaceItemAt(adapterPosition, messageCopy)
    }

    private fun getSupportedViewTypesForChatRv() = listOf(
        MessageViewType(
            onMessageLongClick = { messagePosition ->
                clickedMessagePosition = messagePosition
                dialogWithReactions.show(supportFragmentManager, null)
            },
            onEmojiClick = { message, adapterPosition, reactionInContainerPosition ->
                onReactionUnderMessageClick(message, adapterPosition, reactionInContainerPosition)
            }
        ),
        DateDividerViewType()
    )
}
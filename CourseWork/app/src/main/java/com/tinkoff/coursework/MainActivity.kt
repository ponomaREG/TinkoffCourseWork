package com.tinkoff.coursework

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.tinkoff.coursework.adapter.DelegateAdapter
import com.tinkoff.coursework.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.adapter.viewtype.DateDividerViewType
import com.tinkoff.coursework.adapter.viewtype.MessageViewType
import com.tinkoff.coursework.databinding.ActivityMainBinding
import com.tinkoff.coursework.dialog.BottomSheetDialogWithReactions
import com.tinkoff.coursework.model.Emoji
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.model.Reaction
import com.tinkoff.coursework.util.MockUtil
import com.tinkoff.coursework.util.hideKeyboard
import kotlin.random.Random

class MainActivity : AppCompatActivity(), BottomSheetDialogWithReactions.OnEmojiPickListener {

    private val chatAdapter = DelegateAdapter(getSupportedViewTypesForChatRv())

    private lateinit var dialogWithReactions: BottomSheetDialogWithReactions
    private lateinit var binding: ActivityMainBinding

    private var clickedMessagePosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initRecyclerView()
        initBottomSheetDialogFragmentWithReactions()
        setTextWatcher()
        setListener()
        chatAdapter.setItems(
            MockUtil.mockMessages()
        )
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
                alreadyExistsReaction.apply {
                    countOfVotes += 1
                    isSelected = true
                }
                message.reactions[alreadyExistsReactionInd] = alreadyExistsReaction
            }
            chatAdapter.updateAt(clickedMessagePosition, message)
        }
    }

    private fun initRecyclerView() {
        binding.rvMessages.adapter = chatAdapter
        binding.rvMessages.addItemDecoration(
            OffsetItemDecorator(
                left = 0,
                right = 0,
                top = resources.getDimensionPixelSize(R.dimen.chat_items_margin_top),
                bottom = resources.getDimensionPixelSize(R.dimen.chat_items_margin_bottom)
            )
        )
    }

    private fun initBottomSheetDialogFragmentWithReactions() {
        dialogWithReactions = BottomSheetDialogWithReactions.newInstance()
    }

    private fun setTextWatcher() {
        binding.chatInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val iconForShowing =
                    if (s?.isEmpty() == true) R.drawable.ic_add_files
                    else R.drawable.ic_send_message
                binding.chatBtnAction.setImageResource(iconForShowing)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
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
        chatAdapter.updateAt(adapterPosition, messageCopy)
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
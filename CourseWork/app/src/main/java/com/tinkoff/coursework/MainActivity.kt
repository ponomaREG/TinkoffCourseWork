package com.tinkoff.coursework

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tinkoff.coursework.adapter.DelegateAdapter
import com.tinkoff.coursework.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.adapter.holder.MessageViewHolder
import com.tinkoff.coursework.adapter.viewtype.DateDividerViewType
import com.tinkoff.coursework.adapter.viewtype.EmojiViewType
import com.tinkoff.coursework.adapter.viewtype.MessageViewType
import com.tinkoff.coursework.databinding.ActivityMainBinding
import com.tinkoff.coursework.model.Message
import com.tinkoff.coursework.model.Reaction
import com.tinkoff.coursework.util.MockUtil
import com.tinkoff.coursework.util.hideKeyboard

class MainActivity : AppCompatActivity() {

    private val chatAdapter = DelegateAdapter(getSupportedViewTypesForChatRv())
    private val emojiAdapter = DelegateAdapter(getSupportedViewTypesForBottomSheetDialogWithEmojies())

    private lateinit var dialogWithReactions: BottomSheetDialog
    private lateinit var binding: ActivityMainBinding

    private var clickedMessagePosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initBottomSheetDialogWithReactions()
        attachAdapters()
        attachDecorator()
        setTextWatcher()
        setListener()
        chatAdapter.setItems(
            MockUtil.mockMessages()
        )
    }

    private fun attachAdapters() {
        binding.rvMessages.adapter = chatAdapter
    }

    private fun attachDecorator() {
        binding.rvMessages.addItemDecoration(OffsetItemDecorator(
            left = 0,
            right = 0,
            top = resources.getDimensionPixelSize(R.dimen.chat_items_margin_top),
            bottom = resources.getDimensionPixelSize(R.dimen.chat_items_margin_bottom)
        ))
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
                binding.chatBtnAction.tag = iconForShowing
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun setListener() {
        binding.chatBtnAction.setOnClickListener {
            if (binding.chatBtnAction.tag == R.drawable.ic_send_message) {
                sendMessage()
                binding.chatInput.text = SpannableStringBuilder("")
                hideKeyboard()
            }
        }
    }

    private fun sendMessage() {
        val textMessage = binding.chatInput.text.toString()
        chatAdapter.addItems(listOf(Message(
            username = "Пользователь",
            message = textMessage,
            avatarRes = R.mipmap.ic_launcher,
            reactions = mutableListOf()
        )))
    }

    private fun initBottomSheetDialogWithReactions() {
        dialogWithReactions = BottomSheetDialog(this)
        dialogWithReactions.setContentView(R.layout.bottom_sheet_dialog_reactions)
        dialogWithReactions
            .findViewById<RecyclerView>(R.id.bsd_rv_reactions)?.adapter = emojiAdapter
        emojiAdapter.addItems(MockUtil.mockEmojies())
    }

    private fun onMessageLongClick(messagePosition: Int) {
        clickedMessagePosition = messagePosition
        dialogWithReactions.show()
    }

    private fun onEmojiAtBottomSheetDialogClick(emoji: Int) {
        dialogWithReactions.dismiss()
        val holder = binding.rvMessages.findViewHolderForAdapterPosition(clickedMessagePosition)
        if (holder is MessageViewHolder) {
            holder.addReaction(
                Reaction(
                    emojiCode = emoji,
                    userIdsWhoClicked = mutableListOf(123),
                    isSelected = true,
                    countOfVotes = 1
                )
            )
        }
    }

    private fun getSupportedViewTypesForChatRv() = listOf(
        MessageViewType(this::onMessageLongClick),
        DateDividerViewType()
    )

    private fun getSupportedViewTypesForBottomSheetDialogWithEmojies() = listOf(
        EmojiViewType(this::onEmojiAtBottomSheetDialogClick)
    )
}
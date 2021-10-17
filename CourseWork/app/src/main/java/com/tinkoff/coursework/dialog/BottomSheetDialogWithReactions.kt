package com.tinkoff.coursework.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinkoff.coursework.adapter.DelegateAdapter
import com.tinkoff.coursework.adapter.viewtype.EmojiViewType
import com.tinkoff.coursework.databinding.BottomSheetDialogReactionsBinding
import com.tinkoff.coursework.model.Emoji
import com.tinkoff.coursework.util.MockUtil

class BottomSheetDialogWithReactions(
    onEmojiClick: (Emoji) -> Unit
) : BottomSheetDialogFragment() {

    private val emojiAdapter = DelegateAdapter(
        listOf(
            EmojiViewType(onEmojiClick)
        )
    )

    private lateinit var binding: BottomSheetDialogReactionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            BottomSheetDialogReactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bsdRvReactions.adapter = emojiAdapter
        emojiAdapter.setItems(MockUtil.mockEmojies())
    }
}
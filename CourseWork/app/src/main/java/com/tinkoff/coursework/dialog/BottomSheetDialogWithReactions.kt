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

class BottomSheetDialogWithReactions : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): BottomSheetDialogWithReactions {
            return BottomSheetDialogWithReactions()
        }
    }

    private lateinit var emojiAdapter: DelegateAdapter

    private var _binding: BottomSheetDialogReactionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        emojiAdapter = DelegateAdapter(
            listOf(
                EmojiViewType { emoji ->
                    (activity as? OnEmojiPickListener)?.onEmojiPicked(emoji)
                }
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            BottomSheetDialogReactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bsdRvReactions.adapter = emojiAdapter
        emojiAdapter.setItems(MockUtil.mockEmojies())
    }

    interface OnEmojiPickListener {
        fun onEmojiPicked(emoji: Emoji)
    }
}
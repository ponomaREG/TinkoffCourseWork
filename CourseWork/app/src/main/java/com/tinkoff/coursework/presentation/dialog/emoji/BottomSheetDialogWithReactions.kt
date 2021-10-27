package com.tinkoff.coursework.presentation.dialog.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinkoff.coursework.databinding.BottomSheetDialogReactionsBinding
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.viewtype.EmojiViewType
import com.tinkoff.coursework.presentation.model.Emoji
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable

@AndroidEntryPoint
class BottomSheetDialogWithReactions : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): BottomSheetDialogWithReactions {
            return BottomSheetDialogWithReactions()
        }
    }

    private val viewModel: BSDReactionViewModel by viewModels()

    private lateinit var emojiAdapter: DelegateAdapter

    private var _binding: BottomSheetDialogReactionsBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.bsdRvReactions.adapter = emojiAdapter
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        viewModel.clear()
    }

    private fun observeState() {
        compositeDisposable.add(
            viewModel.stateObservable.subscribe { state ->
                state.apply {
                    isLoading?.let {
                        if (it) {
                            binding.bsdReactionsShimmer.apply {
                                visibility = View.VISIBLE
                                startShimmer()
                            }
                        } else {
                            binding.bsdReactionsShimmer.apply {
                                visibility = View.GONE
                                stopShimmer()
                            }
                        }
                    }
                    emojies?.let {
                        emojiAdapter.setItems(it)
                    }
                    error?.let {
                        requireContext().showToast(it.message)
                    }
                }
            }
        )
    }

    interface OnEmojiPickListener {
        fun onEmojiPicked(emoji: Emoji)
    }
}
package com.tinkoff.coursework.presentation.dialog.emoji

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinkoff.coursework.databinding.BottomSheetDialogReactionsBinding
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.viewtype.EmojiViewType
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.model.Emoji
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
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
        observeAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun observeState() {
        viewModel.stateObservable
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                state.apply {
                    binding.bsdReactionsShimmer.apply {
                        isVisible = state.loadingState == LoadingState.LOADING
                        if (isVisible) startShimmer() else stopShimmer()
                    }
                    emojies?.let {
                        emojiAdapter.setItems(it)
                    }
                }
            }.addTo(compositeDisposable)
    }

    private fun observeAction() {
        viewModel.actionObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { action ->
                when (action) {
                    is BSDAction.ShowToastMessage -> requireContext().showToast(action.message)
                }
            }.addTo(compositeDisposable)
    }

    interface OnEmojiPickListener {
        fun onEmojiPicked(emoji: Emoji)
    }
}
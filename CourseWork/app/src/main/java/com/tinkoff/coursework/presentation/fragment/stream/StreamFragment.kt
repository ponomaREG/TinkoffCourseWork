package com.tinkoff.coursework.presentation.fragment.stream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import com.tinkoff.coursework.databinding.FragmentSpecificStreamsBinding
import com.tinkoff.coursework.presentation.activity.chat.ChatActivity
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.viewtype.StreamViewType
import com.tinkoff.coursework.presentation.adapter.viewtype.TopicViewType
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.model.StreamUI
import com.tinkoff.coursework.presentation.model.StreamsGroup
import com.tinkoff.coursework.presentation.model.TopicUI
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

@AndroidEntryPoint
class StreamFragment : ElmFragment<StreamEvent, StreamAction, StreamUIState>() {

    companion object {
        private const val ARGS_TYPE = "ARGS_TYPE"
        private const val ARGS_FILTER_STRING = "ARGS_FILTER_STRING"

        fun newInstance(type: StreamsGroup): StreamFragment {
            val fragment = StreamFragment()
            val args = Bundle()
            args.putSerializable(ARGS_TYPE, type)
            fragment.arguments = args
            return fragment
        }

        fun updateFilterArguments(inputString: String): Bundle {
            return Bundle().apply {
                putString(ARGS_FILTER_STRING, inputString)
            }
        }
    }

    @Inject
    lateinit var actor: StreamActor

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var _binding: FragmentSpecificStreamsBinding? = null
    private val binding get() = _binding!!

    private val streamAdapter: DelegateAdapter = DelegateAdapter(
        listOf(
            StreamViewType(
                onStreamClick = { stream ->
                    store.accept(
                        StreamEvent.Ui.StreamClick(stream)
                    )
                }
            ),
            TopicViewType(
                onTopicClick = { topic ->
                    store.accept(
                        StreamEvent.Ui.TopicClick(topic)
                    )
                }
            )
        )
    )

    private val type: StreamsGroup
        get() = requireArguments().getSerializable(ARGS_TYPE) as StreamsGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpecificStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        subscribeToFilter()
    }

    override fun onPause() {
        super.onPause()
        binding.streamShimmer.stopShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun initRecyclerView() {
        binding.rvSpecificStreams.apply {
            adapter = streamAdapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    override val initEvent: StreamEvent
        get() = StreamEvent.Ui.LoadStreams(type)

    override fun createStore(): Store<StreamEvent, StreamAction, StreamUIState> =
        ElmStoreCompat(
            initialState = StreamUIState(),
            reducer = StreamReducer(),
            actor = actor
        )

    override fun render(state: StreamUIState) {
        state.apply {
            binding.rvSpecificStreams.isInvisible =
                state.loadingState == LoadingState.LOADING
            binding.streamShimmer.apply {
                isVisible = state.loadingState == LoadingState.LOADING
                if (isVisible) startShimmer() else stopShimmer()
            }
            data?.let {
                streamAdapter.setItems(it)
            }
        }
    }

    override fun handleEffect(effect: StreamAction): Unit = when (effect) {
        is StreamAction.ShowChatActivity -> showChatActivity(effect.stream, effect.topic)
        is StreamAction.ShotToastMessage -> requireContext().showToast(effect.message)
    }

    private fun subscribeToFilter() {
        parentFragmentManager.setFragmentResultListener(
            type.key,
            viewLifecycleOwner
        ) { _, args ->
            val searchInput = args.getString(ARGS_FILTER_STRING)
            searchInput?.let {
                store.accept(
                    StreamEvent.Ui.FilterStreams(it)
                )
            }
        }
    }

    private fun showChatActivity(stream: StreamUI, topic: TopicUI) {
        activity?.let { context ->
            context.startActivity(
                ChatActivity.getIntent(context, stream, topic)
            )
        }
    }
}
package com.tinkoff.coursework.presentation.fragment.stream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.tinkoff.coursework.databinding.FragmentSpecificStreamsBinding
import com.tinkoff.coursework.presentation.activity.chat.ChatActivity
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.viewtype.StreamViewType
import com.tinkoff.coursework.presentation.adapter.viewtype.TopicViewType
import com.tinkoff.coursework.presentation.assisted_factory.StreamAssistedFactory
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.model.Stream
import com.tinkoff.coursework.presentation.model.StreamsGroup
import com.tinkoff.coursework.presentation.model.Topic
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@AndroidEntryPoint
class StreamFragment : Fragment() {

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
    lateinit var streamAssistedFactory: StreamAssistedFactory

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val viewModel: StreamViewModel by viewModels {
        streamAssistedFactory.also {
            it.type = type
        }
    }

    private var _binding: FragmentSpecificStreamsBinding? = null
    private val binding get() = _binding!!

    private val streamAdapter: DelegateAdapter = DelegateAdapter(
        listOf(
            StreamViewType(
                onStreamClick = { stream ->
                    viewModel.onStreamClick(stream)
                }
            ),
            TopicViewType(
                onTopicClick = { topic ->
                    viewModel.onTopicClick(topic)
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
        observeState()
        observeAction()
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

    private fun observeState() {
        viewModel.stateObservable
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
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
            }.addTo(compositeDisposable)
    }

    private fun observeAction() {
        viewModel.actionObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { action ->
                action?.let {
                    when (it) {
                        is StreamAction.ShowChatActivity -> showChatActivity(it.stream, it.topic)
                        is StreamAction.ShotToastMessage -> requireContext().showToast(it.message)
                    }
                }
            }.addTo(compositeDisposable)
    }

    private fun subscribeToFilter() {
        parentFragmentManager.setFragmentResultListener(
            type.key,
            viewLifecycleOwner
        ) { _, args ->
            val searchInput = args.getString(ARGS_FILTER_STRING)
            searchInput?.let {
                viewModel.filterStreams(it)
            }
        }
    }

    private fun showChatActivity(stream: Stream, topic: Topic) {
        activity?.let { context ->
            context.startActivity(
                ChatActivity.getIntent(context, stream, topic)
            )
        }
    }
}
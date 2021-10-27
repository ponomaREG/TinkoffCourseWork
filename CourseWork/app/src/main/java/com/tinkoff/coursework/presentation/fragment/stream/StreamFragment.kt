package com.tinkoff.coursework.presentation.fragment.stream

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.tinkoff.coursework.databinding.FragmentSpecificStreamsBinding
import com.tinkoff.coursework.presentation.activity.chat.ChatActivity
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.viewtype.StreamViewType
import com.tinkoff.coursework.presentation.adapter.viewtype.TopicViewType
import com.tinkoff.coursework.presentation.model.Stream
import com.tinkoff.coursework.presentation.model.StreamsGroup
import com.tinkoff.coursework.presentation.model.Topic
import dagger.hilt.android.AndroidEntryPoint
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
    lateinit var assistedFactory: StreamViewModel.AssistedFactory

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val viewModel: StreamViewModel by viewModels {
        StreamViewModel.provideFactory(
            assistedFactory,
            type
        )
    }

    private var _binding: FragmentSpecificStreamsBinding? = null
    private val binding get() = _binding!!

    private val streamAdapter: DelegateAdapter = DelegateAdapter(
        listOf(
            StreamViewType(
                onStreamClick = { stream, adapterPosition ->
                    viewModel.onStreamClick(stream, adapterPosition)
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
        val callBack: (StreamUIState) -> Unit = { state ->
            state.apply {
                isFirstLoading?.let {
                    if (it) {
                        binding.apply {
                            streamShimmer.visibility = View.VISIBLE
                            streamShimmer.startShimmer()
                            rvSpecificStreams.visibility = View.INVISIBLE
                        }
                    } else {
                        binding.apply {
                            if (streamShimmer.isShimmerStarted) streamShimmer.stopShimmer()
                            streamShimmer.visibility = View.GONE
                            rvSpecificStreams.visibility = View.VISIBLE
                        }
                    }
                }
                streams?.let {
                    streamAdapter.setItems(it)
                }
            }
        }
        compositeDisposable.add(
            viewModel.stateObservable.subscribe(callBack)
        )
    }

    private fun observeAction() {
        val callBack: (StreamAction?) -> Unit = { action ->
            action?.let {
                when (it) {
                    is StreamAction.ShowChatActivity -> showChatActivity(it.stream, it.topic)
                    is StreamAction.AddTopicsAt -> streamAdapter.addItems(it.position, it.topics)
                    is StreamAction.RemoveTopics -> streamAdapter.removeSlice(it.slice)
                    is StreamAction.UpdateStreamAtSpecificPosition ->
                        streamAdapter.replaceItemAt(it.position, it.stream)
                }
            }
        }
        compositeDisposable.add(
            viewModel.actionObservable.subscribe(callBack)
        )
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
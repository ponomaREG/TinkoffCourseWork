package com.tinkoff.coursework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.tinkoff.coursework.ChatActivity
import com.tinkoff.coursework.adapter.DelegateAdapter
import com.tinkoff.coursework.adapter.viewtype.StreamViewType
import com.tinkoff.coursework.adapter.viewtype.TopicViewType
import com.tinkoff.coursework.databinding.FragmentSpecificStreamsBinding
import com.tinkoff.coursework.model.EntityUI
import com.tinkoff.coursework.model.Stream
import com.tinkoff.coursework.model.StreamsGroup
import com.tinkoff.coursework.model.Topic
import com.tinkoff.coursework.util.MockUtil
import java.util.*

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

    private var _binding: FragmentSpecificStreamsBinding? = null
    private val binding get() = _binding!!

    private val streamAdapter: DelegateAdapter = DelegateAdapter(
        listOf(
            StreamViewType(this::onStreamClick),
            TopicViewType(this::onTopicClick)
        )
    )

    private val type: StreamsGroup
        get() = requireArguments().getSerializable(ARGS_TYPE) as StreamsGroup

    private val streams: MutableList<Stream> by lazy {
        (if (type == StreamsGroup.SUBSCRIBED) MockUtil.mockFavoriteStreams()
        else MockUtil.mockAllStreams()).toMutableList()
    }

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
        streamAdapter.setItems(streams)
        subscribeToFilter()
    }

    private fun initRecyclerView() {
        binding.root.apply {
            adapter = streamAdapter
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun subscribeToFilter() {
        parentFragmentManager.setFragmentResultListener(
            type.key,
            viewLifecycleOwner
        ) { _, args ->
            val searchInput = args.getString(ARGS_FILTER_STRING)
            if (searchInput.isNullOrEmpty().not()) {
                val filteredStreams = streams.filter { item ->
                    (item.name
                        .toLowerCase(Locale.ROOT)
                        .contains(searchInput!!.toLowerCase(Locale.ROOT))
                            )
                }
                streamAdapter.setItems(filteredStreams)
            } else streamAdapter.setItems(streams as List<EntityUI>)
        }
    }

    private fun onStreamClick(stream: Stream, adapterPosition: Int) {
        val newStream = stream.copy()
        if (newStream.isExpanded) {
            newStream.isExpanded = false
            val itemsForDeletingIndexes =
                (adapterPosition + 1..newStream.topics.size + adapterPosition)
            streamAdapter.removeSlice(itemsForDeletingIndexes)
        } else {
            newStream.isExpanded = true
            streamAdapter.addItems(adapterPosition + 1, newStream.topics)
        }
        streamAdapter.replaceItemAt(adapterPosition, newStream)
    }

    private fun onTopicClick(topic: Topic) {
        val stream: Stream = streams.find {
            it.topics.contains(topic)
        } ?: throw IllegalStateException()
        activity?.let { context ->
            context.startActivity(
                ChatActivity.getIntent(context, stream, topic)
            )
        }
    }
}
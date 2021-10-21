package com.tinkoff.coursework.fragment

import android.os.Bundle
import android.util.Log
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
import com.tinkoff.coursework.model.Topic
import com.tinkoff.coursework.model.TypeOfStreamsSorting
import java.util.*
import kotlin.collections.ArrayList

class FragmentSpecificStreams : Fragment() {

    companion object {
        private const val ARGS_STREAMS = "ARGS_STREAMS"
        private const val ARGS_TYPE = "ARGS_TYPE"

        fun newInstance(streams: List<Stream>, type: TypeOfStreamsSorting): FragmentSpecificStreams {
            val fragment = FragmentSpecificStreams()
            val args = Bundle()
            args.putParcelableArrayList(ARGS_STREAMS, ArrayList(streams))
            args.putSerializable(ARGS_TYPE, type)
            fragment.arguments = args
            return fragment
        }
    }

    private var _binding: FragmentSpecificStreamsBinding? = null
    val binding get() = _binding!!

    private val streamAdapter: DelegateAdapter = DelegateAdapter(
        listOf(
            StreamViewType(this::onStreamClick),
            TopicViewType(this::onTopicClick)
        )
    )

    private lateinit var streams: MutableList<Stream>
    private lateinit var type: TypeOfStreamsSorting

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        streams = requireArguments().getParcelableArrayList<Stream>(
            ARGS_STREAMS
        )?.toMutableList() ?: throw IllegalStateException("Important argument not found")
        type = (requireArguments().getSerializable(
            ARGS_TYPE
        ) as? TypeOfStreamsSorting) ?: throw IllegalStateException("Important argument not found")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSpecificStreamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.adapter = streamAdapter
        binding.rvSpecificStreams.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
        streamAdapter.setItems(streams)
        parentFragmentManager.setFragmentResultListener(
            type.key,
            viewLifecycleOwner
        ) { _, args ->
            val searchInput = args.getString(FragmentNavigationViaStreams.ARGS_SEARCH_INPUT)
            if (searchInput.isNullOrEmpty().not()) {
                val filteredStreams = streams.filter { item ->
                    (item.name
                        .toLowerCase(Locale.ROOT)
                        .contains(
                            searchInput!!.toLowerCase(Locale.ROOT)
                        )
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
        streamAdapter.updateAt(adapterPosition, newStream)
    }

    private fun onTopicClick(topic: Topic) {
        val stream: Stream = streams.find {
            it.topics.contains(topic)
        } ?: throw IllegalStateException()
        activity?.let { context ->
            ChatActivity.startActivity(context, stream, topic)
        }
    }
}
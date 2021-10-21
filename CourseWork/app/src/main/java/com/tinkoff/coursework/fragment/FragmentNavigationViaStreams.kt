package com.tinkoff.coursework.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.FragmentContainerStreamsBinding
import com.tinkoff.coursework.model.TypeOfStreamsSorting
import com.tinkoff.coursework.util.MockUtil
import com.tinkoff.coursework.viewpager.StreamsStateAdapter

class FragmentNavigationViaStreams : Fragment() {

    companion object {
        const val ARGS_SEARCH_INPUT = "ARGS_SEARCH_INPUT"

        fun newInstance(): FragmentNavigationViaStreams {
            return FragmentNavigationViaStreams()
        }
    }

    private var _binding: FragmentContainerStreamsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContainerStreamsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stateAdapter = StreamsStateAdapter(
            parentFragmentManager,
            lifecycle
        )
        stateAdapter.setFragments(
            listOf(
                FragmentSpecificStreams.newInstance(
                    MockUtil.mockFavoriteStreams(),
                    TypeOfStreamsSorting.SUBSCRIBED
                ),
                FragmentSpecificStreams.newInstance(
                    MockUtil.mockAllStreams(),
                    TypeOfStreamsSorting.ALL
                )
            )
        )
        binding.fragmentContainerStreamsViewpagerStreams.adapter = stateAdapter
        val tabs: List<String> = listOf(
            resources.getString(R.string.fragment_streams_type_subscribed),
            resources.getString(R.string.fragment_streams_type_all),
        )
        TabLayoutMediator(
            binding.fragmentContainerStreamsTabLayout,
            binding.fragmentContainerStreamsViewpagerStreams
        ) { tab, position ->
            tab.text = tabs[position]
        }.attach()
        attachTextWatcher()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun attachTextWatcher() {
        val args = Bundle()
        binding.fragmentSearchesSearchInput.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.let { input ->
                        args.putString(ARGS_SEARCH_INPUT, input.toString())
                        parentFragmentManager.setFragmentResult(
                            TypeOfStreamsSorting.SUBSCRIBED.key,
                            args
                        )
                        parentFragmentManager.setFragmentResult(
                            TypeOfStreamsSorting.ALL.key,
                            args
                        )
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }
            }
        )
    }
}
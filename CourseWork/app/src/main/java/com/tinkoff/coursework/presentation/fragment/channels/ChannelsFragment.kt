package com.tinkoff.coursework.presentation.fragment.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.FragmentContainerStreamsBinding
import com.tinkoff.coursework.presentation.fragment.stream.StreamFragment
import com.tinkoff.coursework.presentation.model.StreamsGroup
import com.tinkoff.coursework.presentation.util.doAfterTextChangedWithDelay
import com.tinkoff.coursework.presentation.viewpager.StreamsStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelsFragment : Fragment() {

    companion object {

        fun newInstance(): ChannelsFragment {
            return ChannelsFragment()
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
        initStateAdapter()
        attachTabLayoutToViewPager()
        attachTextWatcher()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initStateAdapter() {
        val stateAdapter = StreamsStateAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        )
        stateAdapter.setFragments(
            listOf(
                StreamFragment.newInstance(StreamsGroup.SUBSCRIBED),
                StreamFragment.newInstance(StreamsGroup.ALL)
            )
        )
        binding.viewPager.adapter = stateAdapter
    }

    private fun attachTabLayoutToViewPager() {
        val tabs: List<String> = listOf(
            resources.getString(R.string.fragment_streams_type_subscribed),
            resources.getString(R.string.fragment_streams_type_all),
        )
        TabLayoutMediator(
            binding.fragmentContainerStreamsTabLayout,
            binding.viewPager
        ) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun attachTextWatcher() {
        binding.fragmentSearchesSearchInput.doAfterTextChangedWithDelay { input ->
            val args = StreamFragment.updateFilterArguments(input)
            childFragmentManager.setFragmentResult(
                StreamsGroup.SUBSCRIBED.key,
                args
            )
            childFragmentManager.setFragmentResult(
                StreamsGroup.ALL.key,
                args
            )
        }
    }
}
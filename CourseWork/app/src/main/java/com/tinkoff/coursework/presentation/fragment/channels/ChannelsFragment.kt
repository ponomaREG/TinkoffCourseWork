package com.tinkoff.coursework.presentation.fragment.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.FragmentContainerStreamsBinding
import com.tinkoff.coursework.presentation.activity.main.MainActivity
import com.tinkoff.coursework.presentation.fragment.stream.StreamFragment
import com.tinkoff.coursework.presentation.model.StreamsGroup
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.doAfterTextChangedWithDelay
import com.tinkoff.coursework.presentation.viewpager.StreamsStateAdapter
import io.reactivex.disposables.CompositeDisposable

/**
 * Фрагмент с каналами
 * Содержит ViewPager с 2 фрагментами с каналами
 * @see com.tinkoff.coursework.presentation.fragment.stream.StreamFragment
 * Считает вертикальные отступы у вьюгрупы
 */
class ChannelsFragment : Fragment() {

    companion object {

        fun newInstance(): ChannelsFragment {
            return ChannelsFragment()
        }
    }

    private var _binding: FragmentContainerStreamsBinding? = null
    private val binding get() = _binding!!
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

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
        attachListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
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
        }.addTo(compositeDisposable)
    }

    private fun attachListeners() {
        binding.fragmentSearchesIconCreate.setOnClickListener {
            (requireActivity() as? MainActivity)?.showCreateFragment()
        }
    }
}
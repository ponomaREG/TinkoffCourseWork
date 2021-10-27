package com.tinkoff.coursework.presentation.fragment.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.FragmentPeopleBinding
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.presentation.adapter.viewtype.UserViewType
import com.tinkoff.coursework.presentation.util.doAfterTextChangedWithDelay
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@AndroidEntryPoint
class PeopleFragment : Fragment() {

    companion object {
        fun newInstance(): PeopleFragment {
            return PeopleFragment()
        }
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PeopleViewModel by viewModels()
    private val compositeDisposable = CompositeDisposable()

    private val peopleAdapter = DelegateAdapter(
        listOf(
            UserViewType(
                onUserClick = {
                    viewModel.onUserClick(it)
                }
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerView()
        attachTextWatcher()
        observerState()
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

    private fun initRecyclerView() {
        binding.recyclerView.adapter = peopleAdapter
        binding.recyclerView.addItemDecoration(
            OffsetItemDecorator(
                top = resources.getDimensionPixelSize(R.dimen.item_user_margin_top),
                bottom = resources.getDimensionPixelSize(R.dimen.item_user_margin_bottom),
                left = resources.getDimensionPixelSize(R.dimen.item_user_margin_left),
                right = resources.getDimensionPixelSize(R.dimen.item_user_margin_right),
            )
        )
    }

    private fun attachTextWatcher() {
        binding.fragmentPeopleSearchInput.doAfterTextChangedWithDelay { input ->
            viewModel.filter(input)
        }
    }

    private fun observerState() {
        val disposable = viewModel.stateObservable.subscribe { state ->
            state.apply {
                isLoading?.let {
                    if (it) {
                        binding.apply {
                            recyclerView.visibility = View.INVISIBLE
                            peopleShimmer.visibility = View.VISIBLE
                            peopleShimmer.startShimmer()
                        }
                    } else {
                        binding.apply {
                            recyclerView.visibility = View.VISIBLE
                            peopleShimmer.stopShimmer()
                            peopleShimmer.visibility = View.GONE
                        }
                    }
                }
                peoples?.let {
                    peopleAdapter.setItems(it)
                }
                error?.let {
                    requireContext().showToast(it.message)
                }
            }
        }
        compositeDisposable.add(disposable)
    }
}
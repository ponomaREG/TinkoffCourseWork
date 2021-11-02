package com.tinkoff.coursework.presentation.fragment.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.FragmentPeopleBinding
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.presentation.adapter.viewtype.UserViewType
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.doAfterTextChangedWithDelay
import com.tinkoff.coursework.presentation.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

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
    private val compositeDisposableRxJava3: io.reactivex.rxjava3.disposables.CompositeDisposable =
        io.reactivex.rxjava3.disposables.CompositeDisposable() // Мигрировать в дальнейшем полностью на RxJava3

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
        observeState()
        observeActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        compositeDisposableRxJava3.dispose()
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
        }.addTo(compositeDisposableRxJava3)
    }

    private fun observeState() {
        viewModel.stateObservable
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state ->
                state.apply {
                    binding.recyclerView.isInvisible = state.loadingState == LoadingState.LOADING
                    binding.peopleShimmer.apply {
                        isVisible = state.loadingState == LoadingState.LOADING
                        if (isVisible) startShimmer() else stopShimmer()
                    }
                    peoples?.let {
                        peopleAdapter.setItems(it)
                    }
                }
            }.addTo(compositeDisposable)
    }

    private fun observeActions() {
        viewModel.actionObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { action ->
                when (action) {
                    is PeopleAction.ShowToastMessage ->
                        requireContext().showToast(action.message)
                    is PeopleAction.ShowUserProfile ->
                        requireContext().showToast(action.user.toString())
                }
            }.addTo(compositeDisposable)
    }
}
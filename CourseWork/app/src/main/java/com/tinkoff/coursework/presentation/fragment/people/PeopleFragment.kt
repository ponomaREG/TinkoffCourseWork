package com.tinkoff.coursework.presentation.fragment.people

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.tinkoff.coursework.R
import com.tinkoff.coursework.databinding.FragmentPeopleBinding
import com.tinkoff.coursework.getAppComponent
import com.tinkoff.coursework.presentation.adapter.DelegateAdapter
import com.tinkoff.coursework.presentation.adapter.decorator.OffsetItemDecorator
import com.tinkoff.coursework.presentation.adapter.viewtype.UserViewType
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.di.people.DaggerPeopleComponent
import com.tinkoff.coursework.presentation.di.people.PeopleComponent
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.detectStatusColor
import com.tinkoff.coursework.presentation.util.doAfterTextChangedWithDelay
import com.tinkoff.coursework.presentation.util.showToast
import io.reactivex.disposables.CompositeDisposable
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.ElmStoreCompat
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject


class PeopleFragment : ElmFragment<PeopleEvent, PeopleAction, PeopleUIState>() {

    companion object {
        fun newInstance(): PeopleFragment {
            return PeopleFragment()
        }
    }

    @Inject
    lateinit var peopleActor: PeopleActor

    private lateinit var peopleComponent: PeopleComponent

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()

    private val peopleAdapter = DelegateAdapter(
        listOf(
            UserViewType(
                onUserClick = {
                    store.accept(
                        PeopleEvent.Ui.UserClick(it)
                    )
                }
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        peopleComponent = DaggerPeopleComponent.factory().create(getAppComponent())
        peopleComponent.inject(this)
    }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override val initEvent: PeopleEvent
        get() = PeopleEvent.Ui.LoadPeople

    override fun createStore(): Store<PeopleEvent, PeopleAction, PeopleUIState> =
        ElmStoreCompat(
            initialState = PeopleUIState(),
            reducer = PeopleReducer(),
            actor = peopleActor
        )

    override fun render(state: PeopleUIState) {
        state.apply {
            binding.recyclerView.isInvisible = state.loadingState == LoadingState.LOADING
            binding.peopleShimmer.apply {
                isVisible = state.loadingState == LoadingState.LOADING
                if (isVisible) startShimmer() else stopShimmer()
            }
            if (filteredPeople == null) {
                people?.let {
                    it.forEach { user ->
                        user.colorStateList = ColorStateList.valueOf(
                            user.status.detectStatusColor(requireContext())
                        )
                    }
                    peopleAdapter.setItems(it)
                }
            } else {
                filteredPeople!!.forEach { user ->
                    user.colorStateList = ColorStateList.valueOf(
                        user.status.detectStatusColor(requireContext())
                    )
                }
                peopleAdapter.setItems(filteredPeople!!)
            }
        }
    }

    override fun handleEffect(effect: PeopleAction) = when (effect) {
        is PeopleAction.ShowToastMessage ->
            requireContext().showToast(effect.message)
        is PeopleAction.ShowUserProfile ->
            requireContext().showToast(effect.user.toString())
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
            store.accept(
                PeopleEvent.Ui.FilterPeople(input)
            )
        }.addTo(compositeDisposable)
    }
}
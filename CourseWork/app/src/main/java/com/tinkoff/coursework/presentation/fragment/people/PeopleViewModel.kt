package com.tinkoff.coursework.presentation.fragment.people

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.GetPeopleUseCase
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.mapper.UserMapper
import com.tinkoff.coursework.presentation.model.UserUI
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.filterAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val getPeopleUseCase: GetPeopleUseCase,
    private val userMapper: UserMapper
) : ViewModel() {

    val stateObservable: BehaviorSubject<PeopleUIState> = BehaviorSubject.create()
    val actionObservable: PublishSubject<PeopleAction> = PublishSubject.create()
    private var currentState: PeopleUIState = PeopleUIState()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadPeople()
    }

    private var users: List<UserUI>? = null

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun loadPeople() {
        currentState.loadingState = LoadingState.LOADING
        submitState()
        getPeopleUseCase()
            .subscribeOn(Schedulers.io())
            .map { users ->
                users.map(userMapper::fromDomainModelToPresentationModel)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, error ->
                data?.let {
                    users = data
                    currentState.peoples = data
                    currentState.loadingState = LoadingState.SUCCESS
                }
                error?.let { e ->
                    currentState.loadingState = LoadingState.ERROR
                    submitAction(PeopleAction.ShowToastMessage(e.parseError().message))
                }
                submitState()
            }.addTo(compositeDisposable)
    }

    fun filter(searchInput: String) {
        users?.let {
            if (searchInput.isNotEmpty()) {
                it.filterAsync(
                    predicate = { user ->
                        user.fullName.toLowerCase(Locale.ROOT).contains(searchInput)
                    },
                    action = { filteredPeople ->
                        filteredPeople?.let {
                            currentState.peoples = it as List<UserUI>
                            submitState()
                        }
                    }
                )
            } else {
                currentState.peoples = users
                submitState()
            }
        }
    }

    fun onUserClick(user: UserUI) {
        submitAction(PeopleAction.ShowUserProfile(user))
    }

    private fun submitAction(action: PeopleAction) {
        actionObservable.onNext(action)
    }

    private fun submitState() {
        val newState = currentState.copy()
        stateObservable.onNext(currentState)
        currentState = newState
    }
}
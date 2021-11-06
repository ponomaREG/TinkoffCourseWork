package com.tinkoff.coursework.presentation.fragment.profile

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.GetOwnProfileInfoUseCase
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.mapper.UserMapper
import com.tinkoff.coursework.presentation.util.addTo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
    private val userMapper: UserMapper
) : ViewModel() {

    val stateObservable: BehaviorSubject<ProfileUIState> = BehaviorSubject.create()
    val actionObservable: PublishSubject<ProfileAction> = PublishSubject.create()
    private var currentState = ProfileUIState()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadProfileInfo()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun loadProfileInfo() {
        currentState.loadingState = LoadingState.LOADING
        submitState()
        getOwnProfileInfoUseCase()
            .subscribeOn(Schedulers.io())
            .map(userMapper::fromDomainModelToPresentationModel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { user, error ->
                user?.let {
                    currentState.loadingState = LoadingState.SUCCESS
                    currentState.data = it
                }
                error?.let { e ->
                    currentState.loadingState = LoadingState.ERROR
                    submitAction(ProfileAction.ShowToastMessage(e.parseError().message))
                }
                submitState()
            }.addTo(compositeDisposable)
    }

    private fun submitState() {
        val newState = currentState.copy()
        stateObservable.onNext(currentState)
        currentState = newState
    }

    private fun submitAction(action: ProfileAction) {
        actionObservable.onNext(action)
    }
}
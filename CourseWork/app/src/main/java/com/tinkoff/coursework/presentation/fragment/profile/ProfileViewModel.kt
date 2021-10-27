package com.tinkoff.coursework.presentation.fragment.profile

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.GetOwnProfileInfoUseCase
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase
) : ViewModel() {

    val stateObservable: BehaviorSubject<ProfileUIState> = BehaviorSubject.create()
    private val currentState = ProfileUIState()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadProfileInfo()
    }

    fun loadProfileInfo() {
        currentState.isLoading = true
        submitState()
        val callBack: (User?, Throwable?) -> Unit = { user, error ->
            user?.let {
                currentState.isLoading = false
                currentState.data = it
            }
            error?.let {
                currentState.isLoading = false
                currentState.error = it.parseError()
            }
            submitState()
        }
        val disposable = getOwnProfileInfoUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callBack)
        compositeDisposable.add(disposable)
    }

    fun onLogoutClick() {

    }

    fun clear() {
        compositeDisposable.dispose()
    }

    private fun submitState() {
        stateObservable.onNext(currentState)
    }
}
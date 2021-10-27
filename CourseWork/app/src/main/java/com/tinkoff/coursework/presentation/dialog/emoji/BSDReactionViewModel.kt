package com.tinkoff.coursework.presentation.dialog.emoji

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.GetEmojiesUseCase
import com.tinkoff.coursework.presentation.error.parseError
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class BSDReactionViewModel @Inject constructor(
    private val getEmojiesUseCase: GetEmojiesUseCase
) : ViewModel() {

    val stateObservable: BehaviorSubject<BSDReactionUIState> = BehaviorSubject.create()
    private val currentState = BSDReactionUIState()

    private val compositeDisposable = CompositeDisposable()

    init {
        loadEmojies()
    }

    fun loadEmojies() {
        currentState.isLoading = true
        submitState()
        val disposable = getEmojiesUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, err ->
                data?.let {
                    currentState.apply {
                        isLoading = false
                        emojies = it
                    }
                }
                err?.let {
                    currentState.apply {
                        isLoading = false
                        error = it.parseError()
                    }
                }
                submitState()
                currentState.isLoading = null
            }
        compositeDisposable.add(disposable)
    }

    fun clear() {
        compositeDisposable.dispose()
    }

    private fun submitState() {
        stateObservable.onNext(currentState)
    }
}
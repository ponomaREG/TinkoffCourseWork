package com.tinkoff.coursework.presentation.dialog.emoji

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.GetEmojiesUseCase
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.mapper.EmojiMapper
import com.tinkoff.coursework.presentation.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BSDReactionViewModel @Inject constructor(
    private val getEmojiesUseCase: GetEmojiesUseCase,
    private val emojiMapper: EmojiMapper
) : ViewModel() {

    val stateObservable: BehaviorSubject<BSDReactionUIState> = BehaviorSubject.create()
    val actionObservable: PublishSubject<BSDAction> = PublishSubject.create()
    private var currentState = BSDReactionUIState()

    private val compositeDisposable = CompositeDisposable()

    init {
        loadEmojies()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    private fun loadEmojies() {
        currentState.loadingState = LoadingState.LOADING
        submitState()
        getEmojiesUseCase()
            .subscribeOn(Schedulers.computation())
            .map {
                it.map(emojiMapper::fromDomainModelToPresentationModel)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, err ->
                data?.let {
                    currentState.emojies = it
                    currentState.loadingState = LoadingState.SUCCESS
                }
                err?.let {
                    currentState.loadingState = LoadingState.ERROR
                    submitAction(BSDAction.ShowToastMessage(it.parseError().messageId))
                }
                submitState()
            }.addTo(compositeDisposable)
    }

    private fun submitState() {
        val newState = currentState.copy()
        stateObservable.onNext(currentState)
        currentState = newState
    }

    private fun submitAction(action: BSDAction) {
        actionObservable.onNext(action)
    }
}
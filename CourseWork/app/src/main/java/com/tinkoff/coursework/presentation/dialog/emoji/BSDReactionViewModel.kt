package com.tinkoff.coursework.presentation.dialog.emoji

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.GetEmojiesUseCase
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.exception.parseException
import com.tinkoff.coursework.presentation.mapper.EmojiMapper
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.whenCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Вьюмоделька для
 * @see BottomSheetDialogWithReactions
 */
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
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, err ->
                data?.let { response ->
                    response.whenCase(
                        isSuccess = { list ->
                            currentState.emojies =
                                list.map(emojiMapper::fromDomainModelToPresentationModel)
                            currentState.loadingState = LoadingState.SUCCESS
                        },
                        isError = {
                            currentState.loadingState = LoadingState.ERROR
                            submitAction(BSDAction.ShowToastMessage(it.messageId))
                        }
                    )
                }
                err?.let {
                    currentState.loadingState = LoadingState.ERROR
                    submitAction(BSDAction.ShowToastMessage(it.parseException().messageId))
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
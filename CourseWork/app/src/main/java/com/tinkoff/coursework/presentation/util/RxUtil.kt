package com.tinkoff.coursework.presentation.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <E> Collection<E>.filterAsync(
    predicate: (E) -> Boolean,
    action: (Collection<E>?) -> Unit
): Disposable = Observable.fromIterable(this)
    .subscribeOn(Schedulers.computation())
    .filter(predicate)
    .toList()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(action)

fun io.reactivex.rxjava3.disposables.Disposable.addTo(
    compositeDisposable: io.reactivex.rxjava3.disposables.CompositeDisposable
) {
    compositeDisposable.add(this)
}
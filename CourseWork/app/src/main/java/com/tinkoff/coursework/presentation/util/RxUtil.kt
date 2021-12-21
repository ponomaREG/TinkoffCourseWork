package com.tinkoff.coursework.presentation.util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Функция-расширение
 * Добавление в композитный контейнер
 */
fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

/**
 * Функция-расширение
 * Асинхронная фильтрация
 * @param predicate - условие, по которому будем проходить фильтрация
 */
fun <E> Collection<E>.filterAsync(
    predicate: (E) -> Boolean,
    action: (Collection<E>?) -> Unit
): Disposable = Observable.fromIterable(this)
    .subscribeOn(Schedulers.computation())
    .filter(predicate)
    .toList()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(action)

package com.tinkoff.coursework.presentation.fragment.people

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.GetPeoplesUseCase
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val getPeoplesUseCase: GetPeoplesUseCase
) : ViewModel() {

    val stateObservable: BehaviorSubject<PeopleUIState> = BehaviorSubject.create()
    private val currentState: PeopleUIState = PeopleUIState()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadPeoples()
    }

    private var peoples: List<User>? = null

    fun loadPeoples() {
        currentState.isLoading = true
        submitState()
        val callBack: (List<User>?, Throwable?) -> Unit = { data, error ->
            data?.let {
                peoples = data
                currentState.peoples = data
                currentState.isLoading = false
            }
            error?.let {
                currentState.error = it.parseError()
                currentState.isLoading = false
            }
            submitState()
            currentState.isLoading = null
        }
        val disposable = getPeoplesUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(callBack)
        compositeDisposable.add(disposable)
    }

    fun filter(searchInput: String) {
        peoples?.let {
            val filteredPeoples =
                if (searchInput.isNotEmpty()) {
                    it.filter { user ->
                        user.name.toLowerCase(Locale.ROOT).contains(searchInput)
                    }
                } else it
            currentState.peoples = filteredPeoples
            submitState()
        }
    }

    private fun submitState() {
        stateObservable.onNext(currentState)
    }

    fun onUserClick(user: User) {

    }

    fun clear() {
        compositeDisposable.dispose()
    }
}
package com.tinkoff.coursework.presentation.fragment.stream

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.coursework.domain.usecase.GetAllChannelsUseCase
import com.tinkoff.coursework.domain.usecase.GetSubscribedChannelsUseCase
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.model.Stream
import com.tinkoff.coursework.presentation.model.StreamsGroup
import com.tinkoff.coursework.presentation.model.Topic
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*

class StreamViewModel @AssistedInject constructor(
    @Assisted private val type: StreamsGroup,
    private val getSubscribedChannelsUseCase: GetSubscribedChannelsUseCase,
    private val getAllChannelsUseCase: GetAllChannelsUseCase
) : ViewModel() {

    companion object {

        fun provideFactory(
            assistedFactory: AssistedFactory,
            type: StreamsGroup
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(type) as T
            }
        }
    }

    val stateObservable: BehaviorSubject<StreamUIState> = BehaviorSubject.create()
    val actionObservable: PublishSubject<StreamAction> = PublishSubject.create()
    private val currentState = StreamUIState()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadStreamsByType()
    }

    private var streams: List<Stream>? = null

    fun onStreamClick(stream: Stream, adapterPosition: Int) {
        val newStream = stream.copy()
        if (newStream.isExpanded) {
            newStream.isExpanded = false
            val itemsForDeletingIndexes =
                (adapterPosition + 1..newStream.topics.size + adapterPosition)
            submitAction(StreamAction.RemoveTopics(itemsForDeletingIndexes))
        } else {
            newStream.isExpanded = true
            submitAction(StreamAction.AddTopicsAt(newStream.topics, adapterPosition + 1))
        }
        submitAction(StreamAction.UpdateStreamAtSpecificPosition(newStream, adapterPosition))
    }

    fun onTopicClick(topic: Topic) {
        streams?.let {
            val stream: Stream = it.find {
                it.topics.contains(topic)
            } ?: throw IllegalStateException()
            submitAction(StreamAction.ShowChatActivity(stream, topic))
        }
    }

    fun filterStreams(searchInput: String) {
        streams?.let {
            if (searchInput.isNotEmpty()) {
                val filteredStreams = it.filter { item ->
                    (item.name
                        .toLowerCase(Locale.ROOT)
                        .contains(searchInput.toLowerCase(Locale.ROOT))
                        )
                }
                currentState.streams = filteredStreams
            } else currentState.streams = streams
            submitState()
        }
    }

    fun clear() {
        compositeDisposable.dispose()
    }

    private fun loadStreamsByType() {
        currentState.isFirstLoading = true
        submitState()
        val callBack: (List<Stream>?, Throwable?) -> Unit = { data, err ->
            data?.let {
                streams = it
                currentState.apply {
                    isFirstLoading = false
                    streams = it
                }
                submitState()
                currentState.isFirstLoading = null
            }
            err?.let {
                currentState.apply {
                    isFirstLoading = false
                    error = it.parseError()
                }
                submitState()
                currentState.isFirstLoading = null
            }
        }
        val disposable = when (type) {
            StreamsGroup.ALL ->
                getAllChannelsUseCase()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callBack)
            StreamsGroup.SUBSCRIBED ->
                getSubscribedChannelsUseCase()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callBack)
        }
        compositeDisposable.add(disposable)
    }

    private fun submitAction(action: StreamAction) {
        actionObservable.onNext(action)
    }

    private fun submitState() {
        stateObservable.onNext(
            currentState
        )
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(type: StreamsGroup): StreamViewModel
    }
}
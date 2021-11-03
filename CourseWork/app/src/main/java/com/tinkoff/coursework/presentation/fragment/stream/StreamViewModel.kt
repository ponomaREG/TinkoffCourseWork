package com.tinkoff.coursework.presentation.fragment.stream

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.GetAllChannelsUseCase
import com.tinkoff.coursework.domain.usecase.GetSubscribedChannelsUseCase
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.mapper.StreamMapper
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.StreamUI
import com.tinkoff.coursework.presentation.model.StreamsGroup
import com.tinkoff.coursework.presentation.model.TopicUI
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.filterAsync
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.*

class StreamViewModel @AssistedInject constructor(
    @Assisted private val type: StreamsGroup,
    private val getSubscribedChannelsUseCase: GetSubscribedChannelsUseCase,
    private val getAllChannelsUseCase: GetAllChannelsUseCase,
    private val streamMapper: StreamMapper
) : ViewModel() {

    val stateObservable: BehaviorSubject<StreamUIState> = BehaviorSubject.create()
    val actionObservable: PublishSubject<StreamAction> = PublishSubject.create()
    private var currentState = StreamUIState()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadStreamsByType()
    }

    private var streams: List<StreamUI>? = null
    private var filteredStreams: List<StreamUI>? = null

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun onStreamClick(stream: StreamUI) {
        val newStream = stream.copy(isExpanded = stream.isExpanded.not())
        streams = streams?.map { s ->
            if (s.id == newStream.id) newStream else s
        }
        if (filteredStreams != null) {
            filteredStreams = filteredStreams?.map { s ->
                if (s.id == newStream.id) newStream else s
            }
            currentState.data = buildAdapterItems(filteredStreams)
        } else currentState.data = buildAdapterItems(streams)
        submitState()
    }

    fun onTopicClick(topic: TopicUI) {
        streams?.let {
            val stream: StreamUI = it.find { s ->
                s.topics.contains(topic)
            } ?: throw IllegalStateException()
            submitAction(StreamAction.ShowChatActivity(stream, topic))
        }
    }

    fun filterStreams(searchInput: String) {
        streams?.let { streams ->
            if (searchInput.isNotEmpty()) {
                streams.filterAsync(
                    predicate = { stream ->
                        stream.name
                            .toLowerCase(Locale.ROOT)
                            .contains(searchInput.toLowerCase(Locale.ROOT))

                    },
                    action = { filteredStream ->
                        filteredStream?.let {
                            it as List<StreamUI>
                            filteredStreams = it
                            currentState.data = buildAdapterItems(it)
                            submitState()
                        }
                    }
                )
            } else {
                filteredStreams = null
                currentState.data = buildAdapterItems(streams)
                submitState()
            }
        }
    }

    private fun loadStreamsByType() {
        currentState.loadingState = LoadingState.LOADING
        submitState()
        when (type) {
            StreamsGroup.ALL ->
                getAllChannelsUseCase()
            StreamsGroup.SUBSCRIBED ->
                getSubscribedChannelsUseCase()

        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map {
                it.map(streamMapper::fromDomainModelToPresentationModel)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, err ->
                data?.let {
                    streams = it
                    currentState.loadingState = LoadingState.SUCCESS
                    currentState.data = streams
                }
                err?.let { e ->
                    currentState.loadingState = LoadingState.ERROR
                    submitAction(StreamAction.ShotToastMessage(e.parseError().message))
                }
                submitState()
            }.addTo(compositeDisposable)
    }

    private fun submitAction(action: StreamAction) {
        actionObservable.onNext(action)
    }

    private fun submitState() {
        val newState = currentState.copy()
        stateObservable.onNext(currentState)
        currentState = newState
    }

    private fun buildAdapterItems(streams: List<StreamUI>?): List<EntityUI> {
        val adapterItems: MutableList<EntityUI> = mutableListOf()
        streams?.forEach { s ->
            adapterItems.add(s)
            if (s.isExpanded) {
                adapterItems.addAll(s.topics)
            }
        }
        return adapterItems
    }
}
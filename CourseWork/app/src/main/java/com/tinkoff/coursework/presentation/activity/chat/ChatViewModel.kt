package com.tinkoff.coursework.presentation.activity.chat

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.*
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.mapper.EmojiMapper
import com.tinkoff.coursework.presentation.mapper.MessageMapper
import com.tinkoff.coursework.presentation.mapper.UserMapper
import com.tinkoff.coursework.presentation.model.*
import com.tinkoff.coursework.presentation.util.addTo
import com.tinkoff.coursework.presentation.util.convertToDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class ChatViewModel @AssistedInject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val cacheMessagesUseCase: CacheMessagesUseCase,
    private val getCacheMessagesUseCase: GetCacheMessagesUseCase,
    private val addReactionToMessageUseCase: AddReactionToMessageUseCase,
    private val removeReactionToMessageUseCase: RemoveReactionToMessageUseCase,
    private val getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
    private val messageMapper: MessageMapper,
    private val emojiMapper: EmojiMapper,
    private val userMapper: UserMapper,
    @Assisted private val stream: StreamUI,
    @Assisted private val topic: TopicUI
) : ViewModel() {

    companion object {
        const val PAGINATION_OFFSET = 20
        const val MAX_CACHE_MESSAGES = 50
    }

    val observableState: BehaviorSubject<ChatUIState> = BehaviorSubject.create()
    val observableAction: PublishSubject<ChatAction> = PublishSubject.create()
    private var currentState: ChatUIState = ChatUIState()
    private val compositeDisposable = CompositeDisposable()

    private var olderMessageId: Int = -1

    private var myUserInfo: UserUI? = null

    init {
        initLoading()
    }

    private var clickedMessagePosition: Int = -1
    private lateinit var messages: MutableList<MessageUI>

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun initLoading() {
        currentState.loadingState = LoadingState.LOADING
        updateState()
        getOwnProfileInfoUseCase()
            .subscribeOn(Schedulers.io())
            .map(userMapper::fromDomainModelToPresentationModel)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { userInfo, error ->
                userInfo?.let { userUI ->
                    myUserInfo = userUI
                    loadCacheMessages(stream, topic)
                    loadMessages(stream, topic)
                }
                error?.let { e ->
                    currentState.loadingState = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(e.parseError().message))
                }
                updateState()
            }.addTo(compositeDisposable)
    }

    fun loadMessages(
        stream: StreamUI,
        topic: TopicUI,
    ) {
        currentState.loadingNewMessages = LoadingState.LOADING
        updateState()
        getMessagesUseCase(stream.name, topic.name, olderMessageId, PAGINATION_OFFSET)
            .subscribeOn(Schedulers.io())
            .map {
                it.map {
                    messageMapper.fromDomainModelToPresentationModel(it, myUserInfo!!.id)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, error ->
                data?.let { t ->
                    if (t.size == PAGINATION_OFFSET - 1) submitAction(ChatAction.DisablePagination)
                    if (olderMessageId == -1) messages = t.toMutableList()
                    else messages.addAll(0, t.toMutableList().dropLast(1))
                    olderMessageId = t.first().id
                    currentState.loadingState = LoadingState.SUCCESS
                    currentState.loadingNewMessages = LoadingState.SUCCESS
                    buildMessages() {
                        updateState()
                    }
                    cacheMessages()
                }
                error?.let { e ->
                    currentState.loadingState = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(e.parseError().message))
                    updateState()
                }
            }.addTo(compositeDisposable)

    }

    fun onEmojiAtBottomSheetDialogPicked(emoji: EmojiUI) {
        submitAction(ChatAction.HideBottomSheetDialog)
        val clickedMessage = messages[clickedMessagePosition]
        currentState.loadingInput = LoadingState.LOADING
        updateState()
        addReactionRemotely(
            clickedMessage,
            emoji,
            action = {
                currentState.loadingInput = LoadingState.SUCCESS
                val copyOfMessage = clickedMessage.deepCopy()
                if (isReactionAlreadyAddedIntoMessage(copyOfMessage, emoji)) {
                    val alreadyExistsReaction = copyOfMessage.reactions.find { reactionUI ->
                        reactionUI.emojiUI.emojiCode == emoji.emojiCode
                    }
                    if (alreadyExistsReaction!!.isSelected.not()) {
                        alreadyExistsReaction.apply {
                            usersWhoClicked.add(myUserInfo!!.id)
                            isSelected = true
                        }
                    }
                } else {
                    copyOfMessage.reactions.add(
                        ReactionUI(
                            emojiUI = emoji,
                            usersWhoClicked = mutableListOf(myUserInfo!!.id),
                            isSelected = true
                        )
                    )
                }
                messages[clickedMessagePosition] = copyOfMessage
                buildMessages() {
                    updateState()
                }
            },
            err = {
                currentState.loadingInput = LoadingState.ERROR
            }
        )
    }

    fun onMessageLongClick(clickedMessage: MessageUI) {
        clickedMessagePosition = messages.indexOfFirst {
            it.id == clickedMessage.id
        }
        submitAction(ChatAction.OpenBottomSheetDialog)
    }

    fun onEmojiClick(clickedMessage: MessageUI, reactionInContainerPosition: Int) {
        clickedMessagePosition = messages.indexOfFirst {
            it.id == clickedMessage.id
        }
        val messageCopy = clickedMessage.deepCopy()
        val modelReaction = messageCopy.reactions[reactionInContainerPosition]
        currentState.loadingInput = LoadingState.LOADING
        updateState()
        if (modelReaction.isSelected.not()) {
            addReactionRemotely(
                messageCopy,
                modelReaction.emojiUI,
                action = {
                    currentState.loadingInput = LoadingState.SUCCESS
                    modelReaction.usersWhoClicked.add(myUserInfo!!.id)
                    modelReaction.isSelected = true
                    messages[clickedMessagePosition] = messageCopy
                    buildMessages() {
                        updateState()
                    }
                },
                err = {
                    currentState.loadingInput = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(it.parseError().message))
                }
            )
        } else {
            removeReactionRemotely(
                messageCopy,
                modelReaction.emojiUI,
                action = {
                    currentState.loadingInput = LoadingState.SUCCESS
                    modelReaction.usersWhoClicked.remove(myUserInfo!!.id)
                    modelReaction.isSelected = false
                    if (modelReaction.countOfVotes == 0) {
                        messageCopy.reactions.removeAt(reactionInContainerPosition)
                    }
                    messages[clickedMessagePosition] = messageCopy
                    buildMessages() {
                        updateState()
                    }
                },
                err = {
                    currentState.loadingInput = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(it.parseError().message))
                }
            )
        }
    }

    fun sendMessage(topic: TopicUI, input: String) {
        if (myUserInfo == null) return
        val newMessage = MessageUI(
            id = -1,
            username = myUserInfo!!.fullName,
            message = input,
            avatarUrl = myUserInfo!!.avatarUrl,
            reactions = mutableListOf(),
            senderId = myUserInfo!!.id,
            isMyMessage = true,
            timestamp = System.currentTimeMillis()
        )
        currentState.loadingInput = LoadingState.LOADING
        updateState()
        sendMessageUseCase(listOf(stream.id), topic.name, newMessage) //Заменить в будущем на сервис
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    currentState.loadingInput = LoadingState.SUCCESS
                    response.newMessageId?.let {
                        newMessage.id = it
                        messages.add(newMessage)
                        buildMessages() {
                            updateState()
                        }
                    }
                },
                { e ->
                    currentState.loadingInput = LoadingState.ERROR
                    submitAction(ChatAction.ShowPreviouslyTypedMessage(input))
                    submitAction(ChatAction.ShowToastMessage(e.parseError().message))
                    updateState()
                }
            ).addTo(compositeDisposable)
    }

    private fun loadCacheMessages(stream: StreamUI, topic: TopicUI) {
        getCacheMessagesUseCase(stream.id, topic.name)
            .subscribeOn(Schedulers.io())
            .map {
                it.map { messageDomain ->
                    messageMapper.fromDomainModelToPresentationModel(messageDomain, myUserInfo!!.id)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, error ->
                data?.let {
                    if (it.isNotEmpty()) {
                        currentState.loadingState = LoadingState.SUCCESS
                        messages = it.toMutableList()
                        buildMessages {
                            updateState()
                        }
                    }
                }
                error?.let {

                }
            }.addTo(compositeDisposable)
    }

    private fun cacheMessages() {
        if (messages.size <= MAX_CACHE_MESSAGES) cacheMessagesUseCase.invoke(messages.map {
            messageMapper.fromPresentationModelToDomainModel(it, it.timestamp)
        }, stream.id, topic.name)
            .subscribeOn(Schedulers.io())
            .subscribe().addTo(compositeDisposable)
    }

    private fun addReactionRemotely(
        messageUI: MessageUI,
        emoji: EmojiUI,
        action: () -> Unit,
        err: (Throwable) -> Unit
    ) {
        addReactionToMessageUseCase(
            messageUI.id,
            emojiMapper.fromPresentationModelToDomainModel(emoji)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    action()
                    updateState()
                },
                { e ->
                    err(e)
                    updateState()
                }
            ).addTo(compositeDisposable)
    }

    private fun removeReactionRemotely(
        messageUI: MessageUI,
        emoji: EmojiUI,
        action: () -> Unit,
        err: (Throwable) -> Unit
    ) {
        removeReactionToMessageUseCase(
            messageUI,
            emojiMapper.fromPresentationModelToDomainModel(emoji)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    action()
                    updateState()
                },
                { e ->
                    err(e)
                    updateState()
                }
            ).addTo(compositeDisposable)
    }

    private fun isReactionAlreadyAddedIntoMessage(message: MessageUI, emoji: EmojiUI): Boolean {
        return message.reactions.indexOfFirst { it.emojiUI.emojiCode == emoji.emojiCode } != -1
    }

    private fun buildMessages(onComplete: () -> Unit) {
        Single.fromCallable {
            messages = messages.distinct().toMutableList()
            messages.sortBy { it.id }
            val dateMessages: Map<DateDivider, List<MessageUI>> = messages.groupBy {
                DateDivider(it.timestamp.convertToDate())
            }
            dateMessages.flatMap { (key, value) ->
                val list = mutableListOf<EntityUI>(key)
                list.addAll(value)
                list
            }
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { entities, error ->
                entities?.let {
                    currentState.messages = it
                    onComplete()
                }
                error?.let {

                }
            }.addTo(compositeDisposable)

    }

    private fun submitAction(action: ChatAction) {
        observableAction.onNext(action)
    }

    private fun updateState() {
        val newState = currentState.copy()
        observableState.onNext(currentState)
        currentState = newState
    }
}
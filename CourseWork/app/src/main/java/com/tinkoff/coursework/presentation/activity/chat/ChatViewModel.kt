package com.tinkoff.coursework.presentation.activity.chat

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.domain.usecase.AddReactionToMessageUseCase
import com.tinkoff.coursework.domain.usecase.GetMessagesUseCase
import com.tinkoff.coursework.domain.usecase.RemoveReactionToMessageUseCase
import com.tinkoff.coursework.domain.usecase.SendMessageUseCase
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.const.MY_USER_ID
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.mapper.EmojiMapper
import com.tinkoff.coursework.presentation.mapper.MessageMapper
import com.tinkoff.coursework.presentation.model.*
import com.tinkoff.coursework.presentation.util.addTo
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class ChatViewModel @AssistedInject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val addReactionToMessageUseCase: AddReactionToMessageUseCase,
    private val removeReactionToMessageUseCase: RemoveReactionToMessageUseCase,
    private val messageMapper: MessageMapper,
    private val emojiMapper: EmojiMapper,
    @Assisted private val stream: StreamUI,
    @Assisted private val topic: TopicUI
) : ViewModel() {

    val observableState: BehaviorSubject<ChatUIState> = BehaviorSubject.create()
    val observableAction: PublishSubject<ChatAction> = PublishSubject.create()
    private var currentState: ChatUIState = ChatUIState()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadMessages(stream, topic)
    }

    private var clickedMessagePosition: Int = -1
    private lateinit var messages: MutableList<EntityUI>

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun loadMessages(stream: StreamUI, topic: TopicUI) {
        currentState.loadingState = LoadingState.LOADING
        updateState()
        getMessagesUseCase(stream.name, topic.name)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map {
                it.map {
                    messageMapper.fromDomainModelToPresentationModel(it)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, error ->
                data?.let { t ->
                    messages = t.toMutableList()
                    currentState.loadingState = LoadingState.SUCCESS
                    currentState.messages = t
                }
                error?.let { e ->
                    currentState.loadingState = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(e.parseError().message))
                }
                updateState()
            }.addTo(compositeDisposable)

    }

    fun onEmojiAtBottomSheetDialogPicked(emoji: EmojiUI) {
        submitAction(ChatAction.HideBottomSheetDialog)
        val message = messages[clickedMessagePosition]
        if (message is MessageUI) {
            currentState.loadingInput = LoadingState.LOADING
            updateState()
            addReaction(
                message,
                emoji,
                action = {
                    currentState.loadingInput = LoadingState.SUCCESS
                    val newMessage = message.copy(reactions = message.reactions.toMutableList())
                    val alreadyExistsReactionInd =
                        newMessage.reactions.indexOfFirst { it.emojiUI.emojiCode == emoji.emojiCode }
                    if (alreadyExistsReactionInd == -1) {
                        newMessage.reactions.add(
                            ReactionUI(
                                emojiUI = emoji,
                                usersWhoClicked = mutableListOf(MY_USER_ID)
                            )
                        )
                    } else {
                        val alreadyExistsReaction =
                            newMessage.reactions[alreadyExistsReactionInd].copy()
                        if (alreadyExistsReaction.isSelected.not()) {
                            alreadyExistsReaction.apply {
                                usersWhoClicked.add(MY_USER_ID)
                            }
                            newMessage.reactions[alreadyExistsReactionInd] =
                                alreadyExistsReaction
                        }
                    }
                    messages[clickedMessagePosition] = newMessage
                    currentState.messages = messages
                },
                err = {
                    currentState.loadingInput = LoadingState.ERROR
                }
            )
        }
    }

    fun onMessageLongClick(messagePosition: Int) {
        clickedMessagePosition = messagePosition
        submitAction(ChatAction.OpenBottomSheetDialog)
    }

    fun onEmojiClick(messageUI: MessageUI, adapterPosition: Int, reactionInContainerPosition: Int) {
        val messageCopy = messageUI.copy(reactions = messageUI.reactions.toMutableList())
        val modelReaction = messageCopy.reactions[reactionInContainerPosition].copy(
            usersWhoClicked = messageCopy.reactions[reactionInContainerPosition].usersWhoClicked.toMutableList()
        )
        messageCopy.reactions[reactionInContainerPosition] = modelReaction
        currentState.loadingInput = LoadingState.LOADING
        updateState()
        if (modelReaction.isSelected.not()) {
            addReaction(
                messageCopy,
                modelReaction.emojiUI,
                action = {
                    currentState.loadingInput = LoadingState.SUCCESS
                    modelReaction.usersWhoClicked.add(MY_USER_ID)
                    messages[adapterPosition] = messageCopy
                    currentState.messages = messages

                },
                err = {
                    currentState.loadingInput = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(it.parseError().message))
                }
            )
        } else {
            removeReaction(
                messageCopy,
                modelReaction.emojiUI,
                action = {
                    currentState.loadingInput = LoadingState.SUCCESS
                    modelReaction.usersWhoClicked.remove(MY_USER_ID)
                    if (modelReaction.countOfVotes == 0) {
                        messageCopy.reactions.removeAt(reactionInContainerPosition)
                    }
                    messages[adapterPosition] = messageCopy
                    currentState.messages = messages
                },
                err = {
                    currentState.loadingInput = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(it.parseError().message))
                }
            )
        }
    }

    fun sendMessage(topic: TopicUI, input: String) {
        val newMessage = MessageUI(
            id = -1,
            username = "Пользователь",
            message = input,
            avatarUrl = "https://www.interfax.ru/ftproot/textphotos/2021/03/22/700vv.jpg",
            reactions = mutableListOf(),
            senderId = MY_USER_ID
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
                        currentState.messages = messages
                        updateState()
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

    private fun submitAction(action: ChatAction) {
        observableAction.onNext(action)
    }

    private fun updateState() {
        val newState = currentState.copy()
        observableState.onNext(currentState)
        currentState = newState
    }

    private fun addReaction(
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

    private fun removeReaction(
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
}
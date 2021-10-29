package com.tinkoff.coursework.presentation.activity.chat

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.R
import com.tinkoff.coursework.domain.usecase.AddReactionToMessageUseCase
import com.tinkoff.coursework.domain.usecase.GetMessagesUseCase
import com.tinkoff.coursework.domain.usecase.RemoveReactionToMessageUseCase
import com.tinkoff.coursework.domain.usecase.SendMessageUseCase
import com.tinkoff.coursework.presentation.base.LoadingState
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.model.*
import com.tinkoff.coursework.presentation.util.addTo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val addReactionToMessageUseCase: AddReactionToMessageUseCase,
    private val removeReactionToMessageUseCase: RemoveReactionToMessageUseCase
) : ViewModel() {

    val observableState: BehaviorSubject<ChatUIState> = BehaviorSubject.create()
    val observableAction: PublishSubject<ChatAction> = PublishSubject.create()
    private var currentState: ChatUIState = ChatUIState()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadMessages()
    }

    private var clickedMessagePosition: Int = -1
    private lateinit var messages: MutableList<EntityUI>

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun loadMessages() {
        currentState.loadingState = LoadingState.LOADING
        updateState()
        getMessagesUseCase()
            .subscribeOn(Schedulers.io())
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

    fun onEmojiAtBottomSheetDialogPicked(emoji: Emoji) {
        submitAction(ChatAction.HideBottomSheetDialog)
        val message = messages[clickedMessagePosition]
        if (message is Message) {
            currentState.loadingInput = LoadingState.LOADING
            updateState()
            addReaction(
                message,
                emoji.emojiCode,
                action = {
                    currentState.loadingInput = if (it.isSuccess) {
                        val newMessage = message.copy(reactions = message.reactions.toMutableList())
                        val alreadyExistsReactionInd =
                            newMessage.reactions.indexOfFirst { it.emojiCode == emoji.emojiCode }
                        if (alreadyExistsReactionInd == -1) {
                            newMessage.reactions.add(
                                Reaction(
                                    emojiCode = emoji.emojiCode,
                                    isSelected = true,
                                    countOfVotes = 1
                                )
                            )
                        } else {
                            val alreadyExistsReaction =
                                newMessage.reactions[alreadyExistsReactionInd].copy()
                            if (alreadyExistsReaction.isSelected.not()) {
                                alreadyExistsReaction.apply {
                                    countOfVotes += 1
                                    isSelected = true
                                }
                                newMessage.reactions[alreadyExistsReactionInd] =
                                    alreadyExistsReaction
                            }
                        }
                        messages[clickedMessagePosition] = newMessage
                        currentState.messages = messages
                        LoadingState.SUCCESS
                    } else LoadingState.ERROR
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

    fun onEmojiClick(message: Message, adapterPosition: Int, reactionInContainerPosition: Int) {
        val messageCopy = message.copy(reactions = message.reactions.toMutableList())
        val modelReaction = messageCopy.reactions[reactionInContainerPosition].copy()
        messageCopy.reactions[reactionInContainerPosition] = modelReaction
        currentState.loadingInput = LoadingState.LOADING
        updateState()
        modelReaction.isSelected = modelReaction.isSelected.not()
        if (modelReaction.isSelected) {
            addReaction(
                messageCopy,
                modelReaction.emojiCode,
                action = {
                    currentState.loadingInput = if (it.isSuccess) {
                        modelReaction.countOfVotes += 1
                        messages[adapterPosition] = messageCopy
                        currentState.messages = messages
                        LoadingState.SUCCESS
                    } else LoadingState.ERROR
                },
                err = {
                    currentState.loadingInput = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(it.parseError().message))
                }
            )
        } else {
            removeReaction(
                messageCopy,
                modelReaction.emojiCode,
                action = {
                    currentState.loadingInput = if (it.isSuccess) {
                        modelReaction.countOfVotes -= 1
                        if (modelReaction.countOfVotes == 0) {
                            messageCopy.reactions.removeAt(reactionInContainerPosition)
                        }
                        messages[adapterPosition] = messageCopy
                        currentState.messages = messages
                        LoadingState.SUCCESS
                    } else LoadingState.ERROR
                },
                err = {
                    currentState.loadingInput = LoadingState.ERROR
                    submitAction(ChatAction.ShowToastMessage(it.parseError().message))
                }
            )
        }
    }

    fun sendMessage(topic: Topic, input: String) {
        val newMessage = Message(
            id = Random.nextInt(1, 1000),
            username = "Пользователь",
            message = input,
            avatarRes = R.mipmap.ic_launcher,
            reactions = mutableListOf()
        )
        currentState.loadingInput = LoadingState.LOADING
        updateState()
        sendMessageUseCase(topic.id, newMessage) //Заменить в будущем на сервис
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response, e ->
                response?.let {
                    currentState.loadingInput = if (response.isSuccess) {
                        messages.add(newMessage)
                        currentState.messages = messages
                        LoadingState.SUCCESS
                    } else {
                        submitAction(ChatAction.ShowPreviouslyTypedMessage(input))
                        LoadingState.ERROR
                    }
                }
                e?.let {
                    currentState.loadingInput = LoadingState.ERROR
                    submitAction(ChatAction.ShowPreviouslyTypedMessage(input))
                    submitAction(ChatAction.ShowToastMessage(e.parseError().message))
                }
                updateState()
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

    private fun addReaction(
        message: Message,
        emojiCode: Int,
        action: (Response) -> Unit,
        err: (Throwable) -> Unit
    ) {
        addReactionToMessageUseCase(message.id, emojiCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response, error ->
                response?.let { r ->
                    action(r)
                }
                error?.let { e ->
                    err(e)
                }
                updateState()
            }.addTo(compositeDisposable)
    }

    private fun removeReaction(
        message: Message,
        emojiCode: Int,
        action: (Response) -> Unit,
        err: (Throwable) -> Unit
    ) {
        removeReactionToMessageUseCase(message, emojiCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response, error ->
                response?.let { r ->
                    action(r)
                }
                error?.let { e ->
                    err(e)
                }
                updateState()
            }.addTo(compositeDisposable)
    }
}
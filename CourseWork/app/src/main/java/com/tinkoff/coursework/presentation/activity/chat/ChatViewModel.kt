package com.tinkoff.coursework.presentation.activity.chat

import androidx.lifecycle.ViewModel
import com.tinkoff.coursework.R
import com.tinkoff.coursework.domain.usecase.GetMessagesUseCase
import com.tinkoff.coursework.presentation.error.parseError
import com.tinkoff.coursework.presentation.model.Emoji
import com.tinkoff.coursework.presentation.model.EntityUI
import com.tinkoff.coursework.presentation.model.Message
import com.tinkoff.coursework.presentation.model.Reaction
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase
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

    fun loadMessages() {
        currentState.isFirstLoadingMessages = true
        updateState()
        val disposable = getMessagesUseCase()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data, error ->
                data?.let { t ->
                    messages = t.toMutableList()
                    currentState.isFirstLoadingMessages = false
                    currentState.messages = t
                }
                error?.let { e ->
                    currentState.error = e.parseError()
                }
                updateState()
            }
        compositeDisposable.add(disposable)
    }

    fun onEmojiAtBottomSheetDialogPicked(emoji: Emoji) {
        submitAction(ChatAction.HideBottomSheetDialog)
        val message = messages[clickedMessagePosition]
        if (message is Message) {
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
                val alreadyExistsReaction = newMessage.reactions[alreadyExistsReactionInd].copy()
                if (alreadyExistsReaction.isSelected.not()) {
                    alreadyExistsReaction.apply {
                        countOfVotes += 1
                        isSelected = true
                    }
                    newMessage.reactions[alreadyExistsReactionInd] = alreadyExistsReaction
                }
            }
            messages[clickedMessagePosition] = newMessage
            currentState.messages = messages
            updateState()
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
        modelReaction.isSelected = modelReaction.isSelected.not()
        if (modelReaction.isSelected) modelReaction.countOfVotes += 1
        else {
            modelReaction.countOfVotes -= 1
            if (modelReaction.countOfVotes == 0) {
                messageCopy.reactions.removeAt(reactionInContainerPosition)
            }
        }
        messages[adapterPosition] = messageCopy
        currentState.messages = messages
        updateState()
    }

    fun sendMessage(input: String) {
        messages.add(
            Message(
                id = Random.nextInt(1, 1000),
                username = "Пользователь",
                message = input,
                avatarRes = R.mipmap.ic_launcher,
                reactions = mutableListOf()
            )
        )
        currentState.messages = messages
        updateState()
    }

    fun clear() {
        compositeDisposable.dispose()
    }

    private fun submitAction(action: ChatAction) {
        observableAction.onNext(action)
    }

    private fun updateState() {
        observableState.onNext(currentState)
    }
}
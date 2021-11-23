package com.tinkoff.coursework.presentation.activity.chat

import com.tinkoff.coursework.domain.usecase.*
import com.tinkoff.coursework.presentation.mapper.EmojiMapper
import com.tinkoff.coursework.presentation.mapper.MessageMapper
import com.tinkoff.coursework.presentation.mapper.UserMapper
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat
import java.io.File

class ChatActor constructor(
    private val getOwnProfileInfoUseCase: GetOwnProfileInfoUseCase,
    private val getCacheMessagesUseCase: GetCacheMessagesUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val cacheMessagesUseCase: CacheMessagesUseCase,
    private val addReactionToMessageUseCase: AddReactionToMessageUseCase,
    private val removeReactionToMessageUseCase: RemoveReactionToMessageUseCase,
    private val uploadFileUseCase: UploadFileUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val messageMapper: MessageMapper,
    private val userMapper: UserMapper,
    private val emojiMapper: EmojiMapper,
) : ActorCompat<ChatCommand, ChatEvent> {


    override fun execute(command: ChatCommand): Observable<ChatEvent> = when (command) {

        //Команды для сообщений
        is ChatCommand.LoadMessages -> getMessagesUseCase(
            command.streamName,
            command.topicName,
            command.olderMessageId,
            command.offset,
        ).mapEvents(
            { list ->
                ChatEvent.Internal.MessagesLoaded(
                    list.map {
                        messageMapper.fromDomainModelToPresentationModel(it, command.myUserId)
                    }
                )
            },
            { e ->
                ChatEvent.Internal.ErrorLoading(e)
            }
        )

        is ChatCommand.SendMessage -> sendMessageUseCase(
            listOf(command.streamId),
            command.topicName,
            messageMapper.fromPresentationModelToDomainModel(
                command.message
            )
        )
            .mapEvents(
                { response ->
                    ChatEvent.Internal.SuccessSendMessage(
                        messageMapper.fromDomainModelToPresentationModel(
                            response,
                            command.message.senderId
                        ),
                        response.id
                    )
                },
                { e ->
                    ChatEvent.Internal.ErrorSendMessage(e, command.message)
                }
            )

        is ChatCommand.LoadCacheMessages -> getCacheMessagesUseCase(
            command.streamId,
            command.topicName
        ).mapEvents(
            { list ->
                ChatEvent.Internal.CacheMessagesLoaded(
                    list.map {
                        messageMapper.fromDomainModelToPresentationModel(it, command.myUserId)
                    }
                )
            },
            { e ->
                ChatEvent.Internal.ErrorLoading(e)
            }
        )

        is ChatCommand.CacheMessages -> cacheMessagesUseCase(
            command.messages.map {
                messageMapper.fromPresentationModelToDomainModel(it)
            },
            command.streamId,
            command.topicName
        ).mapEvents(
            ChatEvent.Internal.SuccessCacheMessages,
            ChatEvent.Internal.ErrorCacheMessages
        )

        is ChatCommand.LoadOwnUserInfo -> getOwnProfileInfoUseCase()
            .mapEvents(
                { user ->
                    ChatEvent.Internal.MyUserInfoLoaded(
                        userMapper.fromDomainModelToPresentationModel(user)
                    )
                },
                { e ->
                    ChatEvent.Internal.ErrorLoadingMyUserInfo(e)
                }
            )

        //Команды для реакций
        is ChatCommand.AddReaction -> addReactionToMessageUseCase(
            command.message.id,
            emojiMapper.fromPresentationModelToDomainModel(command.emojiUI)
        )
            .mapEvents(
                ChatEvent.Internal.AddReactionAtUI(command.message, command.emojiUI)
            ) { e ->
                ChatEvent.Internal.ErrorLoading(e)
            }

        is ChatCommand.RemoveReaction -> removeReactionToMessageUseCase(
            command.message.id,
            emojiMapper.fromPresentationModelToDomainModel(command.emojiUI)
        )
            .mapEvents(
                ChatEvent.Internal.RemoveReactionAtUI(command.message, command.emojiUI)
            ) { e ->
                ChatEvent.Internal.ErrorLoading(e)
            }

        //Команды для файлов
        is ChatCommand.UploadFile -> uploadFileUseCase(command.uri)
            .mapEvents(
                { link ->
                    ChatEvent.Internal.FileUploaded(
                        File(link.hyperlink).name,
                        link.hyperlink
                    )
                },
                { e ->
                    ChatEvent.Internal.ErrorLoading(e)
                }
            )
    }
}
package com.tinkoff.coursework.presentation.activity.chat

import com.tinkoff.coursework.domain.usecase.*
import com.tinkoff.coursework.domain.util.MessageContentParser
import com.tinkoff.coursework.presentation.exception.parseException
import com.tinkoff.coursework.presentation.mapper.EmojiMapper
import com.tinkoff.coursework.presentation.mapper.MessageMapper
import com.tinkoff.coursework.presentation.mapper.UserMapper
import com.tinkoff.coursework.presentation.util.whenCase
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
    private val messageContentParser: MessageContentParser,
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
            { response ->
                response.whenCase(
                    isSuccess = {
                        ChatEvent.Internal.MessagesLoaded(
                            it.map {
                                messageMapper.fromDomainModelToPresentationModel(
                                    it,
                                    command.myUserId,
                                    command.topicName != null
                                )
                            }
                        )
                    },
                    isError = {
                        ChatEvent.Internal.ErrorLoading(it)
                    }
                )
            },
            { e ->
                ChatEvent.Internal.ErrorLoading(e.parseException())
            }
        )

        is ChatCommand.SendMessage -> sendMessageUseCase(
            listOf(command.message.streamId),
            messageMapper.fromPresentationModelToDomainModel(
                command.message
            )
        )
            .mapEvents(
                { response ->
                    response.whenCase(
                        isSuccess = {
                            val parseMessageContent = messageContentParser.parseMessageContent(
                                it.message
                            )
                            val parsedMessage = it.copy(
                                message = parseMessageContent.formattedMessage,
                                messageHyperlinks = parseMessageContent.hyperlinks
                            )
                            ChatEvent.Internal.SuccessSendMessage(
                                messageMapper.fromDomainModelToPresentationModel(
                                    parsedMessage,
                                    command.message.senderId,
                                    command.message.isUniqueTopicInAllChat
                                ),
                                parsedMessage.id
                            )

                        },
                        isError = {
                            ChatEvent.Internal.ErrorSendMessage(it,command.message)
                        }
                    )
                },
                { e ->
                    ChatEvent.Internal.ErrorSendMessage(e.parseException(), command.message)
                }
            )

        is ChatCommand.LoadCacheMessages -> getCacheMessagesUseCase(
            command.streamId,
            command.topicName
        ).mapEvents(
            { response ->
                response.whenCase(
                    isSuccess = { list ->
                        ChatEvent.Internal.CacheMessagesLoaded(
                            list.map {
                                messageMapper.fromDomainModelToPresentationModel(
                                    it,
                                    command.myUserId,
                                    command.topicName != null
                                )
                            }
                        )
                    },
                    isError = {
                        ChatEvent.Internal.ErrorLoading(it)
                    }
                )
            },
            { e ->
                ChatEvent.Internal.ErrorLoading(e.parseException())
            }
        )

        is ChatCommand.CacheMessages -> cacheMessagesUseCase(
            command.messages.map {
                messageMapper.fromPresentationModelToDomainModel(it)
            }
        ).mapEvents(
            ChatEvent.Internal.SuccessCacheMessages,
            ChatEvent.Internal.ErrorCacheMessages
        )

        is ChatCommand.LoadOwnUserInfo -> getOwnProfileInfoUseCase()
            .mapEvents(
                { response ->
                    response.whenCase(
                        isSuccess = {
                            ChatEvent.Internal.MyUserInfoLoaded(
                                userMapper.fromDomainModelToPresentationModel(it)
                            )
                        },
                        isError = {
                            ChatEvent.Internal.ErrorLoadingMyUserInfo(it)
                        }
                    )
                },
                { e ->
                    ChatEvent.Internal.ErrorLoadingMyUserInfo(e.parseException())
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
                ChatEvent.Internal.ErrorLoading(e.parseException())
            }

        is ChatCommand.RemoveReaction -> removeReactionToMessageUseCase(
            command.message.id,
            emojiMapper.fromPresentationModelToDomainModel(command.emojiUI)
        )
            .mapEvents(
                ChatEvent.Internal.RemoveReactionAtUI(command.message, command.emojiUI)
            ) { e ->
                ChatEvent.Internal.ErrorLoading(e.parseException())
            }

        //Команды для файлов
        is ChatCommand.UploadFile -> uploadFileUseCase(command.uri)
            .mapEvents(
                { response ->
                    response.whenCase(
                        isSuccess = {
                            ChatEvent.Internal.FileUploaded(
                                File(it.hyperlink).name,
                                it.hyperlink
                            )
                        },
                        isError = {
                            ChatEvent.Internal.ErrorLoading(it)
                        }
                    )
                },
                { e ->
                    ChatEvent.Internal.ErrorLoading(e.parseException())
                }
            )
    }
}
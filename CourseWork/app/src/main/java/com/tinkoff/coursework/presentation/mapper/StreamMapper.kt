package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.Stream
import com.tinkoff.coursework.presentation.base.BaseMapper
import com.tinkoff.coursework.presentation.model.StreamUI
import javax.inject.Inject

class StreamMapper @Inject constructor(
    private val topicMapper: TopicMapper
) : BaseMapper<Stream, StreamUI> {

    override fun fromDomainModelToPresentationModel(domainModel: Stream): StreamUI =
        StreamUI(
            id = domainModel.id,
            name = domainModel.name,
            topics = domainModel.topics.map(topicMapper::fromDomainModelToPresentationModel)
        )

    override fun fromPresentationModelToDomainModel(presentationModel: StreamUI): Stream =
        Stream(
            id = presentationModel.id,
            name = presentationModel.name,
            topics = presentationModel.topics.map(topicMapper::fromPresentationModelToDomainModel)
        )
}
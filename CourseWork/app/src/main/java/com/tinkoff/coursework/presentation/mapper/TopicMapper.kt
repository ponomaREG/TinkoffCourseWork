package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.Topic
import com.tinkoff.coursework.presentation.base.BaseMapper
import com.tinkoff.coursework.presentation.model.TopicUI
import javax.inject.Inject

class TopicMapper @Inject constructor() : BaseMapper<Topic, TopicUI> {

    override fun fromDomainModelToPresentationModel(domainModel: Topic): TopicUI =
        TopicUI(
            name = domainModel.name,
        )

    override fun fromPresentationModelToDomainModel(presentationModel: TopicUI): Topic =
        Topic(
            name = presentationModel.name
        )
}
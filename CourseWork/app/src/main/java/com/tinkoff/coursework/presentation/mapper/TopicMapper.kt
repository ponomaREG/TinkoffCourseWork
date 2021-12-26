package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.Topic
import com.tinkoff.coursework.presentation.model.TopicUI
import javax.inject.Inject

class TopicMapper @Inject constructor() {

    fun fromDomainModelToPresentationModel(domainModel: Topic): TopicUI =
        TopicUI(
            name = domainModel.name,
        )
}
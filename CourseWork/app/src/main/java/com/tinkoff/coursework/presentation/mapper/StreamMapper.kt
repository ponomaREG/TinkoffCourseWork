package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.Stream
import com.tinkoff.coursework.presentation.model.StreamUI
import javax.inject.Inject

class StreamMapper @Inject constructor() {

    fun fromDomainModelToPresentationModel(domainModel: Stream): StreamUI =
        StreamUI(
            id = domainModel.id,
            name = domainModel.name,
        )
}
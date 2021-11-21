package com.tinkoff.coursework.presentation.mapper

import com.tinkoff.coursework.domain.model.MessageHyperlink
import com.tinkoff.coursework.presentation.model.MessageHyperlinkUI
import javax.inject.Inject

class MessageHyperlinkMapper @Inject constructor() {

    fun fromDomainModelToPresentationModel(domainModel: MessageHyperlink) =
        MessageHyperlinkUI(
            from = domainModel.from,
            to = domainModel.to,
            hyperlink = domainModel.hyperlink,
            name = domainModel.name,
        )

    fun fromPresentationModelToDomainModel(presentationModel: MessageHyperlinkUI) =
        MessageHyperlink(
            from = presentationModel.from,
            to = presentationModel.to,
            hyperlink = presentationModel.hyperlink,
            name = presentationModel.name,
        )
}
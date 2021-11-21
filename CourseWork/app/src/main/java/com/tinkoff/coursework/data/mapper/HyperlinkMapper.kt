package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.HyperlinkNetwork
import com.tinkoff.coursework.domain.model.Hyperlink
import javax.inject.Inject

class HyperlinkMapper @Inject constructor() {

    fun fromNetworkModelToDomainModel(hyperlinkNetwork: HyperlinkNetwork): Hyperlink =
        Hyperlink(
            hyperlink = hyperlinkNetwork.hyperlink
        )
}
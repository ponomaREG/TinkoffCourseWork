package com.tinkoff.coursework.data.mapper

import com.tinkoff.coursework.data.network.model.UserUploadsNetwork
import com.tinkoff.coursework.domain.model.Hyperlink
import javax.inject.Inject

class HyperlinkMapper @Inject constructor() {

    fun fromNetworkModelToDomainModel(hyperlinkNetwork: UserUploadsNetwork): Hyperlink =
        Hyperlink(
            hyperlink = hyperlinkNetwork.uri
        )
}
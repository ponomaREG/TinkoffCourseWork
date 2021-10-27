package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.network.MockUtil
import com.tinkoff.coursework.domain.repository.ChannelRepository
import com.tinkoff.coursework.presentation.model.Stream
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor() : ChannelRepository {

    override fun getAllChannels(): Single<List<Stream>> =
        Single.fromCallable(MockUtil::mockAllStreams)
            .subscribeOn(Schedulers.io())
            .delay(1, TimeUnit.SECONDS)

    override fun getSubscribedChannels(): Single<List<Stream>> =
        Single.fromCallable(MockUtil::mockFavoriteStreams)
            .subscribeOn(Schedulers.io())
            .delay(1, TimeUnit.SECONDS)
}
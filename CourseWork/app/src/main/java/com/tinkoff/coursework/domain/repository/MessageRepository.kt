package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.presentation.model.EntityUI
import io.reactivex.Maybe
import io.reactivex.Single

interface MessageRepository {

    fun fetchMessages(): Single<List<EntityUI>>
}
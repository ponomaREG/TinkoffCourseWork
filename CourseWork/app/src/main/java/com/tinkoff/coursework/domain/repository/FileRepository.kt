package com.tinkoff.coursework.domain.repository

import com.tinkoff.coursework.domain.model.Hyperlink
import io.reactivex.Single
import java.io.InputStream

interface FileRepository {

    fun uploadFile(inputStream: InputStream): Single<Hyperlink>
}
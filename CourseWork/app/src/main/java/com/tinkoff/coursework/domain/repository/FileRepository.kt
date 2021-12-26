package com.tinkoff.coursework.domain.repository

import android.net.Uri
import com.tinkoff.coursework.domain.Response
import com.tinkoff.coursework.domain.model.Hyperlink
import io.reactivex.Single

interface FileRepository {

    fun uploadFile(uri: Uri): Single<Response<Hyperlink>>
}
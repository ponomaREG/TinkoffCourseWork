package com.tinkoff.coursework.domain.repository

import android.net.Uri
import com.tinkoff.coursework.domain.model.Hyperlink
import io.reactivex.Single
import java.io.InputStream

interface FileRepository {

    fun uploadFile(uri: Uri): Single<Hyperlink>
}
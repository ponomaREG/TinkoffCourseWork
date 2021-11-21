package com.tinkoff.coursework.data.repository

import com.tinkoff.coursework.data.mapper.HyperlinkMapper
import com.tinkoff.coursework.data.network.api.FileAPI
import com.tinkoff.coursework.domain.model.Hyperlink
import com.tinkoff.coursework.domain.repository.FileRepository
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.InputStream
import javax.inject.Inject


class FileRepositoryImpl @Inject constructor(
    private val fileAPI: FileAPI,
    private val hyperlinkMapper: HyperlinkMapper
) : FileRepository {

    companion object {
        private const val MEDIA_TYPE = "multipart/form-data"
        private const val FILE_NAME = "file"
    }

    override fun uploadFile(inputStream: InputStream): Single<Hyperlink> {
        val requestBody = RequestBody.create(
            "image/*".toMediaTypeOrNull(),
            inputStream.readBytes()
        )
        val body = MultipartBody.Part.createFormData("image", "userImage", requestBody)
        return fileAPI.uploadFile(body).map(hyperlinkMapper::fromNetworkModelToDomainModel)
    }
}
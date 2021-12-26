package com.tinkoff.coursework.data.repository

import android.content.Context
import android.net.Uri
import com.tinkoff.coursework.data.mapper.HyperlinkMapper
import com.tinkoff.coursework.data.network.api.FileAPI
import com.tinkoff.coursework.data.util.mapToResponse
import com.tinkoff.coursework.domain.Response
import com.tinkoff.coursework.domain.model.Hyperlink
import com.tinkoff.coursework.domain.repository.FileRepository
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


class FileRepositoryImpl @Inject constructor(
    private val fileAPI: FileAPI,
    private val hyperlinkMapper: HyperlinkMapper,
    private val context: Context
) : FileRepository {

    companion object {
        private const val MULTIPART_NAME = "image"
        private const val FILE_NAME = "userImage"
    }

    override fun uploadFile(uri: Uri): Single<Response<Hyperlink>> {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes() ?: throw IllegalStateException()
        inputStream.close()
        val requestBody = bytes
            .toRequestBody(
                "image/*".toMediaTypeOrNull(),
                0, bytes.size
            )
        return Single.fromCallable {
            MultipartBody.Part.createFormData(MULTIPART_NAME, FILE_NAME, requestBody)
        }.flatMap { body ->
            fileAPI.uploadFile(body)
        }.map { hyperlink ->
            hyperlinkMapper.fromNetworkModelToDomainModel(hyperlink)
        }.mapToResponse()
    }
}
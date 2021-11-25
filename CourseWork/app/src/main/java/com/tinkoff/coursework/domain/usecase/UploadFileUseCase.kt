package com.tinkoff.coursework.domain.usecase

import android.net.Uri
import com.tinkoff.coursework.domain.repository.FileRepository
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {

    operator fun invoke(uri: Uri) = fileRepository.uploadFile(uri)
}

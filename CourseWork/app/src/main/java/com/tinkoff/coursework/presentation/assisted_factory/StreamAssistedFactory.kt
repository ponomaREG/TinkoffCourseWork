package com.tinkoff.coursework.presentation.assisted_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tinkoff.coursework.presentation.fragment.stream.StreamViewModel
import com.tinkoff.coursework.presentation.model.StreamsGroup
import javax.inject.Inject

class StreamAssistedFactory @Inject constructor(
    private val assistedFactory: AssistedFactory
) : ViewModelProvider.Factory {

    var type: StreamsGroup? = null

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return assistedFactory.create(type!!) as T
    }
}

@dagger.assisted.AssistedFactory
interface AssistedFactory {
    fun create(type: StreamsGroup): StreamViewModel
}
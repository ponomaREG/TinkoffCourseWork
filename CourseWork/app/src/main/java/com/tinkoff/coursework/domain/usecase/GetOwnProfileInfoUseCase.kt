package com.tinkoff.coursework.domain.usecase

import com.tinkoff.coursework.domain.repository.PeopleRepository
import javax.inject.Inject

class GetOwnProfileInfoUseCase @Inject constructor(
    private val peopleRepository: PeopleRepository
) {
    operator fun invoke() = peopleRepository.getOwnProfileInfo()
}
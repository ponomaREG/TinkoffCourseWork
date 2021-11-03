package com.tinkoff.coursework.presentation.base

interface BaseMapper<D, P> {

    fun fromDomainModelToPresentationModel(domainModel: D): P

    fun fromPresentationModelToDomainModel(presentationModel: P): D
}
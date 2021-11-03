package com.tinkoff.coursework.data.base

interface BaseMapper<D, P, N> {

    fun fromNetworkModelToDomainModel(
        networkModel: N
    ): D

    fun fromPersistenceModelToDomainModel(
        persistenceModel: P
    ): D

    fun fromDomainModelToPersistenceModel(
        domainModel: D
    ): P

    fun fromDomainModelToNetworkModel(
        domainModel: D
    ): N
}
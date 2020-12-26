package com.ulusoyapps.venucity.datasource.mapper

interface EntityMapper<C, E> {
    fun mapToDomainEntity(type: C): E
    fun mapToDataLayerEntity(type: E): C
    fun mapToDomainEntityList(type: List<C>): List<E>
    fun mapToDataLayerEntityList(type: List<E>): List<C>
}

package com.ulusoyapps.venucity.cache.mapper

interface EntityMapper<C, E> {
    fun mapToCacheEntity(type: C): E
    fun mapToDataLayerEntity(type: E): C
    fun mapToDataLayerEntityList(type: List<E>): List<C>
    fun mapToCacheLayerEntityList(type: List<C>): List<E>
}

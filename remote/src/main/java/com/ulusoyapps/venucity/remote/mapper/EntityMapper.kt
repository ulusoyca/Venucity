package com.ulusoyapps.venucity.remote.mapper

interface EntityMapper<C, E> {
    fun mapToRemoteEntity(type: C): E
    fun mapToDataLayerEntity(type: E): C
    fun mapToDataLayerEntityList(type: List<E>): List<C>
    fun mapToRemoteEntityList(type: List<C>): List<E>
}

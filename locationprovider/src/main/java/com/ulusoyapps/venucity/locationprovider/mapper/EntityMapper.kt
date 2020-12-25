package com.ulusoyapps.venucity.locationprovider.mapper

interface EntityMapper<C, E> {
    fun mapToLocationProviderEntity(type: C): E
    fun mapToDataLayerEntity(type: E): C
    fun mapToDataLayerEntityList(type: List<C>): List<E>
    fun mapToLocationProviderEntityList(type: List<E>): List<C>
}

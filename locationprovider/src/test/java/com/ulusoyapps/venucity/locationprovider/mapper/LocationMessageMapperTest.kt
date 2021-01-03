package com.ulusoyapps.venucity.locationprovider.mapper

import com.google.common.truth.Truth
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationNotAvailable
import com.ulusoyapps.venucity.datasource.entities.DataLayerLocationReadError
import com.ulusoyapps.venucity.locationprovider.entity.SourceEmpty
import com.ulusoyapps.venucity.locationprovider.entity.SourceNotFound
import com.ulusoyapps.venucity.locationprovider.entity.SourceReadError
import org.junit.Test

class LocationMessageMapperTest {
    @Test
    fun mapToDataLayerEntityList() {
        val mapper = LocationMessageMapper()
        Truth.assertThat(mapper.mapToDataLayerEntity(SourceReadError)).isEqualTo(
            DataLayerLocationReadError
        )
        Truth.assertThat(mapper.mapToDataLayerEntity(SourceNotFound)).isEqualTo(
            DataLayerLocationNotAvailable
        )
        Truth.assertThat(mapper.mapToDataLayerEntity(SourceEmpty)).isEqualTo(
            DataLayerLocationNotAvailable
        )
    }
}

package com.ulusoyapps.venucity.cache.dao

import com.google.common.truth.Truth
import com.ulusoyapps.venucity.cache.entities.CachedLatLng
import com.ulusoyapps.venucity.cache.entities.CachedVenue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VenueDaoTest : VenueDatabaseTest() {

    private val venueDao: VenueDao
        get() = venueTrackerDatabase.venueDao()

    @Test
    fun `should Insert and get new venue`() = runBlocking(Dispatchers.IO) {
        val venue = CachedVenue(
            "id",
            "name",
            "desc",
            "imageUrl",
            CachedLatLng(0.0f, 0.0f),
        )
        venueDao.addVenue(venue)
        val expected = venueDao.getVenue("id")
        Truth.assertThat(venue).isEqualTo(expected)
    }

    @Test
    fun `should return null if venue is not found`() = runBlocking(Dispatchers.IO) {
        val expected = venueDao.getVenue("Unknown")
        Truth.assertThat(expected).isNull()
    }

    @Test
    fun `should Insert And Get All venues`() = runBlocking(Dispatchers.IO) {
        val id1 = CachedVenue(
                "id-1",
                "name",
                "desc",
                "imageUrl",
                CachedLatLng(0.0f, 0.0f),
            )
        val id2 = CachedVenue(
                "id-2",
                "name",
                "desc",
                "imageUrl",
                CachedLatLng(0.0f, 0.0f),
        )
        venueDao.addVenue(id1)
        venueDao.addVenue(id2)
        val allVenues = venueDao.getAllVenues()
        allVenues.take(1).collect {
            Truth.assertThat(it).isEqualTo(listOf(id1, id2))
        }
    }

    @Test
    fun `should get null if no venue is found when all venues are queried`() = runBlocking(Dispatchers.IO) {
        venueDao.getAllVenues().take(1).collect {
            Truth.assertThat(it).isEqualTo(emptyList<CachedVenue>())
        }
    }

    @Test
    fun `should replace if the same id is added`() = runBlocking(Dispatchers.IO) {
        val id = "id"
        val original = CachedVenue(
            id,
            "original name",
            "desc",
            "imageUrl",
            CachedLatLng(0.0f, 0.0f),
        )
        venueDao.addVenue(original)
        val actual = venueDao.getVenue(id)
        Truth.assertThat(actual).isEqualTo(original)

        val updated = original.copy(name = "updated name")
        val result = venueDao.addVenue(updated)
        val actualUpdated = venueDao.getVenue(id)
        Truth.assertThat(actualUpdated).isEqualTo(updated)
        Truth.assertThat(result).isGreaterThan(0)
    }


    @Test
    fun `should return 0 if venue to be deleted not found`() = runBlocking(Dispatchers.IO) {
        val numberOfRowsDeleted = venueDao.removeByVenueId("0")
        Truth.assertThat(numberOfRowsDeleted).isEqualTo(0)
    }

    @Test
    fun `should return 1 if venue to be deleted is found`() = runBlocking(Dispatchers.IO) {
        val venue = CachedVenue(
            "id",
            "name",
            "desc",
            "imageUrl",
            CachedLatLng(0.0f, 0.0f),
        )
        venueDao.addVenue(venue)
        val numberOfRowsDeleted = venueDao.removeByVenueId(venue.id)
        Truth.assertThat(numberOfRowsDeleted).isEqualTo(1)
    }
}
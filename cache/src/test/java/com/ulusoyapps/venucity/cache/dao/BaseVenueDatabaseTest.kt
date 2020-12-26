package com.ulusoyapps.venucity.cache.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ulusoyapps.unittesting.BaseArchTest
import com.ulusoyapps.venucity.cache.database.VenueDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
abstract class VenueDatabaseTest : BaseArchTest() {

    protected lateinit var venueTrackerDatabase: VenueDatabase

    @Before
    fun initDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        venueTrackerDatabase = Room.inMemoryDatabaseBuilder(context, VenueDatabase::class.java).build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        venueTrackerDatabase.close()
    }
}
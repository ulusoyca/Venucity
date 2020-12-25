package com.ulusoyapps.unittesting

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import java.io.InputStream

/**
 * Base test makes background executor used by the Architecture Components to run synchronously
 */
abstract class BaseArchTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @ExperimentalCoroutinesApi
    fun advanceTime(timeMillis: Long) {
        coroutinesTestRule.testDispatcher.advanceTimeBy(timeMillis)
    }

    private fun openResFile(filename: String): InputStream? {
        return this::class.java.classLoader!!.getResource(filename).openStream()
    }

    /**
     * Produces a context that supplies a resource for testing.
     * https://adrianhall.github.io/android/2020/01/03/mockito-mocking-resources/
     */
    fun getTestContext(id: Int, filename: String): Context {
        val resources = mock<Resources> {
            on { openRawResource(eq(id)) } doAnswer {
                openResFile(filename)
            }

            on { getIdentifier(eq(filename), eq("raw"), any()) } doReturn(id)
        }

        return mock {
            on { getResources() } doReturn (resources)
            on { packageName } doReturn (javaClass.canonicalName)
        }
    }
}

package com.ulusoyapps.unittesting

import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import java.io.InputStream
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

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

    // https://github.com/googlecodelabs/kotlin-coroutines/pull/29/files
    fun <T> LiveData<T>.observeForTesting(block: () -> Unit) {
        val observer = Observer<T> { Unit }
        try {
            observeForever(observer)
            block()
        } finally {
            removeObserver(observer)
        }
    }

    // https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-basics#8
    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = SECONDS,
        afterObserve: () -> Unit = {}
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)

        try {
            afterObserve.invoke()

            // Don't wait indefinitely if the LiveData is not set.
            if (!latch.await(time, timeUnit)) {
                throw TimeoutException("LiveData value was never set.")
            }
        } finally {
            this.removeObserver(observer)
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}

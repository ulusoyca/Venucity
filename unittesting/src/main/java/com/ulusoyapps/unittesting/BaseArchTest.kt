package com.ulusoyapps.unittesting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

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
    fun advanceTimeBy(timeMillis: Long) {
        coroutinesTestRule.testDispatcher.advanceTimeBy(timeMillis)
    }
}

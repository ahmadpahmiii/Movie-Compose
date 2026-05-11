package com.example.data.di

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Interface for Coroutine dispatchers.
 *
 * Architectural Decision: Abstracting dispatchers behind an interface allows
 * tests to inject a TestCoroutineDispatcher, making coroutine-based code
 * fully deterministic and avoiding real threading in unit tests.
 */

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}

/**
 * Production implementation using real Android dispatchers.
 * Injected by Hilt in non-test builds.
 */

class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
}
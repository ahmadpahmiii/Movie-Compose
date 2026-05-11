package com.example.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ahmad Pahmi on May 2026
 */

/**
 * Binds [DispatcherProvider] interface to [DefaultDispatcherProvider].
 *
 * In tests, we replace this binding with a TestDispatcherModule that
 * provides UnconfinedTestDispatcher, making all coroutines run synchronously.
 *
 * @Binds generates more efficient code than @Provides because it avoids
 * creating a Provides method body – Hilt directly delegates the binding.
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherModule {

    @Binds
    @Singleton
    abstract fun bindDispatcherProvider(
        defaultDispatcherProvider: DefaultDispatcherProvider
    ): DispatcherProvider
}
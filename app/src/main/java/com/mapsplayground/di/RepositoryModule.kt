package com.mapsplayground.di

import com.mapsplayground.repository.HarborRepository
import com.mapsplayground.repository.harbor.HarborRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideHarborRepository(harborRepositoryImpl: HarborRepositoryImpl): HarborRepository {
        return harborRepositoryImpl
    }
}

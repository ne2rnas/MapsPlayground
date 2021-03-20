package com.mapsplayground.di

import android.content.Context
import androidx.room.Room
import com.mapsplayground.cache.HarborCacheImpl
import com.mapsplayground.cache.db.MapsPlaygroundDatabase
import com.mapsplayground.cache.db.Migrations
import com.mapsplayground.repository.harbor.HarborCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    private const val DATABASE_NAME = "mapsplayground_db"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MapsPlaygroundDatabase {
        return Room
            .databaseBuilder(context, MapsPlaygroundDatabase::class.java, DATABASE_NAME)
            .addMigrations(*Migrations.getMigrations())
            .build()
    }

    @Provides
    fun provideHarborCache(harborCacheImpl: HarborCacheImpl): HarborCache {
        return harborCacheImpl
    }
}

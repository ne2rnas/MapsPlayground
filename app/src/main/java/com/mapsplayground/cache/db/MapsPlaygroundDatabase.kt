package com.mapsplayground.cache.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mapsplayground.cache.dao.HarborDao
import com.mapsplayground.cache.models.harbor.HarborCache

@Database(
    entities = [
        HarborCache::class
    ],
    version = Migrations.DB_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MapsPlaygroundDatabase : RoomDatabase() {
    abstract fun harborDao(): HarborDao
}

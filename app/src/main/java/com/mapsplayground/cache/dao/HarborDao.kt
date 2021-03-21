package com.mapsplayground.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mapsplayground.cache.harbor.models.HarborCache
import io.reactivex.rxjava3.core.Single

@Dao
interface HarborDao {

    @Query("SELECT * FROM harbor ")
    fun getHarbors(): Single<List<HarborCache>>

    @Query("SELECT * FROM harbor LIMIT 1")
    fun loadOneHarbor(): Single<List<HarborCache>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveHarbors(harbors: List<HarborCache>)

    @Query("DELETE FROM harbor")
    fun clearTable()
}

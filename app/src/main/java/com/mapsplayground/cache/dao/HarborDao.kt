package com.mapsplayground.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mapsplayground.cache.models.harbor.HarborCache
import io.reactivex.rxjava3.core.Observable

@Dao
interface HarborDao {

    @Query("SELECT * FROM harbor ")
    fun getHarbors(): Observable<List<HarborCache>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveHarbors(harbors: List<HarborCache>)

    @Query("DELETE FROM harbor")
    fun clearTable()
}

package com.example.hw_catsapi.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatsDao {

    @Query("SELECT * FROM CatEntity")
    suspend fun getCats(): List<CatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCats(list: List<CatEntity>)
}
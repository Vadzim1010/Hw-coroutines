package com.example.hw_catsapi.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hw_catsapi.database.dao.CatsDao
import com.example.hw_catsapi.database.entity.CatEntity

@Database(entities = [CatEntity::class], version = 1)
abstract class CatsDatabase : RoomDatabase() {

    abstract fun getCatsDao(): CatsDao
}

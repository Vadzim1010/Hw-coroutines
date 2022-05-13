package com.example.hw_catsapi

import android.app.Application
import androidx.room.Room
import com.example.hw_catsapi.database.CatsDatabase
import com.example.hw_catsapi.repository.CatsRepository
import com.example.hw_catsapi.retrofit.CatsServiceLocator

class CatsApplication : Application() {

    private val catsApi by lazy { CatsServiceLocator.provideCatsApi() }

    val catsRepository by lazy { CatsRepository(catsApi, appDatabase.getCatsDao()) }


    private var _appDatabase: CatsDatabase? = null
    private val appDatabase get() = requireNotNull(_appDatabase)


    override fun onCreate() {
        super.onCreate()
        _appDatabase = Room
            .databaseBuilder(
                this,
                CatsDatabase::class.java,
                "room_database"
            )
            .build()
    }
}
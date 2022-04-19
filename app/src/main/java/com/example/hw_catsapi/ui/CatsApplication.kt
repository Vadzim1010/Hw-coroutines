package com.example.hw_catsapi.ui

import android.app.Application
import com.example.hw_catsapi.repository.CatsRepository
import com.example.hw_catsapi.retrofit.CatsService

class CatsApplication : Application() {

    private val catsApi by lazy { CatsService.provideCatsApi() }

    val repository by lazy { CatsRepository(catsApi) }
}
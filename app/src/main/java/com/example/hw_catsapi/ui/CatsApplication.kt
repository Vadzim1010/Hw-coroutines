package com.example.hw_catsapi.ui

import android.app.Application
import com.example.hw_catsapi.ui.repository.CatsRepository
import com.example.hw_catsapi.ui.retrofit.CatsService

class CatsApplication : Application() {

    private val catsApi by lazy { CatsService.provideCatsApi() }

    val repository by lazy { CatsRepository(catsApi) }
}
package com.example.hw_catsapi.ui.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object CatsService {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideCatsApi(): CatsApi {
        return retrofit.create()
    }
}
package com.example.hw_catsapi.ui.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object CatsService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val catsApi by lazy {
        retrofit.create<CatsApi>()
    }

    fun provideCatsApi(): CatsApi {
        return catsApi
    }
}
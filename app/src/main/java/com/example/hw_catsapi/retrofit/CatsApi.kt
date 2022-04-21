package com.example.hw_catsapi.retrofit

import com.example.hw_catsapi.retrofit.model.ApiCatsBreeds
import com.example.hw_catsapi.retrofit.model.ApiDescription
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {

    @GET("breeds")
    fun getCatsBreed(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Call<List<ApiCatsBreeds>>

    @GET("images/search")
    fun getBreedById(
        @Query("breed_ids") breedId: String
    ): Call<List<ApiDescription>>
}
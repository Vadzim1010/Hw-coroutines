package com.example.hw_catsapi.ui.retrofit

import com.example.hw_catsapi.ui.retrofit.model.ApiCatsBreeds
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {

    @GET("v1/breeds?x-api-key=b976a021-6e7d-42c6-9314-126cba7706d9")
    fun getCatsBreed(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Call<List<ApiCatsBreeds>>
}
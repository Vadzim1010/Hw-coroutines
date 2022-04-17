package com.example.hw_catsapi.ui.retrofit

import android.graphics.pdf.PdfDocument
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatsApi {

    @GET("/breeds")
    fun getCatsBreed(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): Call<List<ApiCatsBreeds>>
}
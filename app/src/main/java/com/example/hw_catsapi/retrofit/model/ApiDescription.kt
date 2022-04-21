package com.example.hw_catsapi.retrofit.model

import com.google.gson.annotations.SerializedName

data class ApiDescription(
    val breeds: List<Breeds>,
    @SerializedName("url")
    val imageUrl: String
)

data class Breeds(
    val id: String,

    @SerializedName("name")
    val breed: String,

    val description: String,
)

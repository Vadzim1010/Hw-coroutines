package com.example.hw_catsapi.retrofit.model

import com.google.gson.annotations.SerializedName

data class DescriptionResponse(
    @SerializedName("breeds")
    val breeds: List<Breeds>,
    @SerializedName("url")
    val imageUrl: String
)

data class Breeds(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val breed: String,
    @SerializedName("description")
    val description: String,
)

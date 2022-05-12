package com.example.hw_catsapi.retrofit.model

import com.google.gson.annotations.SerializedName

data class CatsBreedsResponse(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val breed: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val imageBreed: ImageBreed?,
)

data class ImageBreed(
    @SerializedName("url")
    val imageBreedUrl: String
)

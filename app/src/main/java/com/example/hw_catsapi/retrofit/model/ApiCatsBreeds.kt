package com.example.hw_catsapi.retrofit.model

import com.google.gson.annotations.SerializedName

data class ApiCatsBreeds(

    val id: String,

    @SerializedName("name")
    val breed: String,

    @SerializedName("image")
    val imageBreed: ImageBreed?
)

data class ImageBreed(

    @SerializedName("url")
    val imageBreedUrl: String
)

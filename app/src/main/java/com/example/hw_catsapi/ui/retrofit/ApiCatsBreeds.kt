package com.example.hw_catsapi.ui.retrofit

import com.google.gson.annotations.SerializedName

data class ApiCatsBreeds(

    val id: Int,

    @SerializedName("name")
    val breed: String,

    @SerializedName("image")
    val imageBreed: ImageBreed?
)

data class ImageBreed(

    @SerializedName("url")
    val imageBreedUrl: String
)

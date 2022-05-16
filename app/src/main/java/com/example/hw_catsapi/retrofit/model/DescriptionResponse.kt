package com.example.hw_catsapi.retrofit.model

import com.example.hw_catsapi.model.CatDescription
import com.google.gson.annotations.SerializedName

data class DescriptionResponse(
    @SerializedName("breeds")
    val breeds: List<Breeds>,
    @SerializedName("url")
    val imageUrl: String
) {


    fun toModel() = CatDescription(
        id = breeds.getOrNull(0)?.id,
        breed = breeds.getOrNull(0)?.breed,
        description = breeds.getOrNull(0)?.description,
        catImageUrl = imageUrl
    )
}

data class Breeds(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val breed: String,
    @SerializedName("description")
    val description: String,
)

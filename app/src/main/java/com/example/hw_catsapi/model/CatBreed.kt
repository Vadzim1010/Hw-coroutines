package com.example.hw_catsapi.model

sealed class Item {

    data class CatBreed(
        val id: String,
        val breed: String,
        val catImageUrl: String?,
    ) : Item()

    object Loading : Item()

    data class Error(
        val error: String
    ) : Item()
}
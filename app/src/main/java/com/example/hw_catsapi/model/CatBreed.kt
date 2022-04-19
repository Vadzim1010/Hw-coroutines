package com.example.hw_catsapi.model

sealed class LoadState {

    data class CatBreed(
        val id: String,
        val breed: String,
        val catImageUrl: String?,
    ) : LoadState()

    object Loading : LoadState()
}
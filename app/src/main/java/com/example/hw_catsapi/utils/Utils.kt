package com.example.hw_catsapi.utils

import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.model.Item
import com.example.hw_catsapi.retrofit.model.ApiCatsBreeds
import com.example.hw_catsapi.retrofit.model.ApiDescription

fun List<ApiCatsBreeds>.mapBreeds(): List<Item.CatBreed> {
    return this.map {
        Item.CatBreed(
            id = it.id,
            breed = it.breed,
            catImageUrl = it.imageBreed?.imageBreedUrl,
        )
    }
}

fun List<ApiDescription>.mapDescription(): List<CatDescription> {
    return this.map {
        CatDescription(
            id = it.breeds.getOrNull(0)?.id,
            breed = it.breeds.getOrNull(0)?.breed,
            description = it.breeds.getOrNull(0)?.description,
            catImageUrl = it.imageUrl,
        )
    }
}
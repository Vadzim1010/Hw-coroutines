package com.example.hw_catsapi.utils

import android.util.Log
import com.example.hw_catsapi.model.CatBreed
import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.model.PagingItem
import com.example.hw_catsapi.retrofit.model.CatsBreedsResponse
import com.example.hw_catsapi.retrofit.model.DescriptionResponse

fun List<CatsBreedsResponse>.mapBreeds(): List<CatBreed> {
    return this.map { apiCatsBreed ->
        CatBreed(
            id = apiCatsBreed.id,
            breed = apiCatsBreed.breed,
            catImageUrl = apiCatsBreed.imageBreed?.imageBreedUrl,
        )
    }
}

fun List<CatBreed>.mapToPage(): List<PagingItem<CatBreed>> {
    return this.map { catBreed ->
        PagingItem.Content(
            data = catBreed
        )
    }
}

fun List<DescriptionResponse>.mapDescription(): List<CatDescription> {
    return this.map {
        CatDescription(
            id = it.breeds.getOrNull(0)?.id,
            breed = it.breeds.getOrNull(0)?.breed,
            description = it.breeds.getOrNull(0)?.description,
            catImageUrl = it.imageUrl,
        )
    }
}

fun log(message: String) {
    Log.i("TAG", message)
}
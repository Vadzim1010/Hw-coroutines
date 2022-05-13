package com.example.hw_catsapi.utils

import android.util.Log
import com.example.hw_catsapi.database.CatEntity
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.model.PagingItem
import com.example.hw_catsapi.retrofit.model.CatsBreedsResponse
import com.example.hw_catsapi.retrofit.model.DescriptionResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun List<CatsBreedsResponse>.mapBreeds(): List<Cat> {
    return this.map { apiCatsBreed ->
        Cat(
            id = apiCatsBreed.id,
            breed = apiCatsBreed.breed,
            catImageUrl = apiCatsBreed.imageBreed?.imageBreedUrl,
        )
    }
}

fun Flow<List<Cat>>.mapToPage(): Flow<List<PagingItem<Cat>>> {
    return this.map { catBreedList ->
        catBreedList.map { catBreed ->
            PagingItem.Content(
                data = catBreed
            )
        }
    }
}

fun List<PagingItem<Cat>>.mapToEntity(): List<CatEntity> =
    this.map {
        it as? PagingItem.Content
    }.map { content ->
        checkNotNull(content)
        CatEntity(
            id = content.data.id,
            breed = content.data.breed,
            catImageUrl = content.data.catImageUrl
        )
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
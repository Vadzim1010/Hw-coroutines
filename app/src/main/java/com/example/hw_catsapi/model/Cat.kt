package com.example.hw_catsapi.model

import com.example.hw_catsapi.database.CatEntity

data class Cat(
    val id: String,
    val breed: String,
    val catImageUrl: String?,
) {
    fun toEntity() = CatEntity(
        id = id,
        breed = breed,
        catImageUrl = catImageUrl
    )
}
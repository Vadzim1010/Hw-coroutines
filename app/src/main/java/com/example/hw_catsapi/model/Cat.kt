package com.example.hw_catsapi.model

import com.example.hw_catsapi.database.entity.CatEntity

data class Cat(
    val id: String,
    val breed: String,
    val catImageUrl: String?,
) {


    fun toEntity(page: Int) = CatEntity(
        id = id,
        breed = breed,
        catImageUrl = catImageUrl,
        page = page
    )

    fun toPagingItem() = PagingItem.Content(
        data = this
    )
}

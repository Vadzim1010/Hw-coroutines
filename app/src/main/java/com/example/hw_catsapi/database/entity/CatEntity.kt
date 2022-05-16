package com.example.hw_catsapi.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hw_catsapi.model.Cat

@Entity
data class CatEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "breed")
    val breed: String,
    @ColumnInfo(name = "cat_image_url")
    val catImageUrl: String?,
    @ColumnInfo(name = "page")
    val page: Int
) {
    fun toModel() = Cat(
        id = id,
        breed = breed,
        catImageUrl = catImageUrl,
    )
}

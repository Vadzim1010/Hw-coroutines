package com.example.hw_catsapi.repository

import com.example.hw_catsapi.model.CatBreed
import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.retrofit.CatsApi
import com.example.hw_catsapi.utils.mapBreeds
import com.example.hw_catsapi.utils.mapDescription
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CatsRepository(private val catsApi: CatsApi) {

    suspend fun fetchBreeds(page: Int): List<CatBreed> = withContext(Dispatchers.IO) {
        val resultList: List<CatBreed>
        try {
            resultList = catsApi.getCatsBreed(page, 20)
                .mapBreeds()
        } catch (e: Throwable) {
            throw e
        }
        resultList.toList()
    }

    suspend fun fetchDescription(breedId: String): List<CatDescription> {
        val resultList: List<CatDescription>
        try {
            resultList = catsApi.getBreedById(breedId)
                .mapDescription()
        } catch (e: Exception) {
            throw e
        }
        return resultList
    }
}

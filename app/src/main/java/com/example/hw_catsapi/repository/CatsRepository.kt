package com.example.hw_catsapi.repository

import com.example.hw_catsapi.database.CatEntity
import com.example.hw_catsapi.database.CatsDao
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.retrofit.CatsApi
import com.example.hw_catsapi.retrofit.model.CatsBreedsResponse
import com.example.hw_catsapi.utils.mapBreeds
import com.example.hw_catsapi.utils.mapDescription
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class CatsRepository(
    private val catsApi: CatsApi,
    private val catsDao: CatsDao
) {

    suspend fun fetchBreeds(page: Int): Flow<List<Cat>> {
//        delay for testing
        delay(1000)
        var responseList: List<CatsBreedsResponse> = listOf()
        runCatching {
            catsApi.getCatsBreed(page, 20)
        }
            .onSuccess {
                responseList = it
            }
            .onFailure {
                throw it
            }
        return flow {
            emit(responseList.mapBreeds())
        }
    }

    suspend fun loadCacheCats(): Flow<List<Cat>> = flow {
        val cacheList = catsDao.getCats()
            .map { it.toModel() }
        emit(cacheList)
    }

    suspend fun insertCacheCats(cacheList: List<CatEntity>) {
        catsDao.insertCats(cacheList)
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

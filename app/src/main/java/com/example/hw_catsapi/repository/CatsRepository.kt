package com.example.hw_catsapi.repository

import com.example.hw_catsapi.database.dao.CatsDao
import com.example.hw_catsapi.database.entity.CatEntity
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.retrofit.CatsApi
import com.example.hw_catsapi.utils.log
import com.example.hw_catsapi.utils.mapToCats
import com.example.hw_catsapi.utils.mapToDescription
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class CatsRepository(
    private val catsApi: CatsApi,
    private val catsDao: CatsDao
) {


    suspend fun fetchCats(page: Int) {
        runCatching {
            delay(2000)
            log("load internet")
            catsApi.getCatsBreed(page, 20)
        }
            .onSuccess { }
            .map { responseList ->
                val cacheList = responseList
                    .mapToCats()
                    .map { catsList ->
                        catsList.toEntity(page)
                    }

                insertCacheCats(cacheList)
            }
            .onFailure { throwable ->
                throw throwable
            }
    }

    suspend fun getCachedCats(limit: Int): Flow<List<Cat>> = flow {
        val cacheList = catsDao.getCats(limit = limit)
            .map { it.toModel() }
        log("load cache")
        emit(cacheList)
    }

    private suspend fun insertCacheCats(cacheList: List<CatEntity>) {
        catsDao.insertCats(cacheList)
    }

    suspend fun fetchDescription(breedId: String): List<CatDescription> = runCatching {
        catsApi.getBreedById(breedId)
    }
        .onSuccess { }
        .map { responseList ->
            responseList.mapToDescription()
        }
        .onFailure { throwable ->
            throw throwable
        }
        .getOrThrow()
}

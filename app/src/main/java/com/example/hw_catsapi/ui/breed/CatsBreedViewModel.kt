package com.example.hw_catsapi.ui.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hw_catsapi.database.CatEntity
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.PagingItem
import com.example.hw_catsapi.repository.CatsRepository
import com.example.hw_catsapi.utils.log
import com.example.hw_catsapi.utils.mapToPage
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

class CatsBreedViewModel(private val repository: CatsRepository) : ViewModel() {

    private var page = 0
    private var isLoading = false
    private var pagingList = listOf<PagingItem<Cat>>()
    private var _sharedFlow = MutableSharedFlow<List<PagingItem<Cat>>>(
        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow = _sharedFlow.asSharedFlow()

    suspend fun fetchPage() {
        var catsCacheList: List<CatEntity> = listOf()
        if (!isLoading && page <= LAST_PAGE) {
            isLoading = true
           val result =  runCatching { repository.fetchBreeds(page) }
                .onSuccess { catsFlow ->
                    catsFlow
                        .onEach { catsList ->
                            catsCacheList = catsList
                                .map { it.toEntity() }

                        }
                        .mapToPage()
                        .onEach { newPagingList ->
                            pagingList = pagingList.dropLast(1)
                                .plus(newPagingList + PagingItem.Loading)

                            if (page == LAST_PAGE) {
                                pagingList = pagingList.dropLast(1)
                            }
                            page++
                            isLoading = false
                        }
                        .launchIn(viewModelScope)

                }
                .onFailure {
                    val errorList = pagingList.dropLast(1)
                        .plus(PagingItem.Error(it))
                    pagingList = errorList
                    isLoading = false
                }.map {
                    it.mapToPage()
                }

        }
        repository.insertCacheCats(catsCacheList)
        _sharedFlow.tryEmit(pagingList)
    }

    suspend fun loadCacheCats() {
        repository.loadCacheCats()
            .mapToPage()
            .onEach {
                log("cache: $it")
                _sharedFlow.tryEmit(it + PagingItem.Loading)
            }
            .launchIn(viewModelScope)
    }

    fun refresh() {
        page = 0
        pagingList = emptyList()
    }

    companion object {
        private const val LAST_PAGE = 3
    }
}

class CatsBreedViewModelFactory(
    private val repository: CatsRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatsBreedViewModel(repository) as T
    }
}
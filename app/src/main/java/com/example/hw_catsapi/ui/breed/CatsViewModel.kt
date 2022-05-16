package com.example.hw_catsapi.ui.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.PagingItem
import com.example.hw_catsapi.repository.CatsRepository
import com.example.hw_catsapi.utils.log
import com.example.hw_catsapi.utils.mapToPage
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class CatsViewModel(private val repository: CatsRepository) : ViewModel() {


    private var page = 0
    private var pagingList = listOf<PagingItem<Cat>>()
    private val _sharedFlow = MutableSharedFlow<List<PagingItem<Cat>>>(
        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow = _sharedFlow.asSharedFlow()


    suspend fun fetchPage() {
        loadCacheCats((page + 1) * 20)
            .map { cacheList ->
                cacheList
                    .plus(PagingItem.Loading)
            }
            .onEach { _sharedFlow.tryEmit(it) }
            .launchIn(viewModelScope)

        runCatching { repository.fetchCats(page) }
            .onSuccess { catsFlow ->
                catsFlow
                    .mapToPage()
                    .map { newPagingList ->
                        pagingList
                            .dropLast(1)
                            .plus(newPagingList)
                            .plus(PagingItem.Loading)
                    }
                    .onEach { pagingList = it }
//                    .onEach { _sharedFlow.tryEmit(pagingList) }
                    .onEach { page++ }
                    .onEach { log(page.toString()) }
                    .onEach { log("load internet") }
//                    .filter { page == 4 }
//                    .onEach { pagingList = pagingList.dropLast(1) }
//                    .onEach { _sharedFlow.tryEmit(pagingList) }
                    .launchIn(viewModelScope)
            }
            .onFailure { throwable ->
                delay(1000)

                loadCacheCats(Int.MAX_VALUE)
                    .map { cacheList ->
                        cacheList
                            .plus(PagingItem.Error(throwable))
                    }
//                    .onEach { _sharedFlow.tryEmit(it) }
                    .launchIn(viewModelScope)
            }
    }

    private suspend fun loadCacheCats(limit: Int): Flow<List<PagingItem<Cat>>> {
        log("load cache")
        return repository.getCachedCats(limit)
            .mapToPage()
    }

    suspend fun refresh() {
        page = 0
        pagingList = emptyList()
        fetchPage()
    }
}


class CatsViewModelFactory(
    private val repository: CatsRepository,
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatsViewModel(repository) as T
    }
}
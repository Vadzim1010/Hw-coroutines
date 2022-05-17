package com.example.hw_catsapi.ui.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.PagingItem
import com.example.hw_catsapi.repository.CatsRepository
import com.example.hw_catsapi.utils.LAST_PAGE
import com.example.hw_catsapi.utils.LIMIT
import com.example.hw_catsapi.utils.log
import com.example.hw_catsapi.utils.mapToPage
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class CatsViewModel(private val repository: CatsRepository) : ViewModel() {


    private var page = 0
    private val _sharedFlow = MutableSharedFlow<List<PagingItem<Cat>>>(
        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow = _sharedFlow.asSharedFlow()


    suspend fun loadNextPage() {
        runCatching { repository.fetchCats(page) }
            .onSuccess {
                loadCacheCats((page + 1) * LIMIT)
                    .map { cacheList ->
                        cacheList
                            .plus(PagingItem.Loading)
                    }
                    .onEach { _sharedFlow.tryEmit(it) }
                    .onEach { page++ }
                    .onEach { log("current page: $page") }
                    .filter { page > LAST_PAGE }
                    .onEach { _sharedFlow.tryEmit(it.dropLast(1)) }
                    .launchIn(viewModelScope)
            }
            .onFailure { throwable ->
                delay(1000)

                loadCacheCats(Int.MAX_VALUE)
                    .map { cacheList ->
                        cacheList
                            .plus(PagingItem.Error(throwable))
                    }
                    .onEach { _sharedFlow.tryEmit(it) }
                    .launchIn(viewModelScope)
            }
    }

    private suspend fun loadCacheCats(limit: Int): Flow<List<PagingItem<Cat>>> {
        return repository.getCachedCats(limit)
            .mapToPage()
    }

    suspend fun refresh() {
        page = 0
        loadNextPage()
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
package com.example.hw_catsapi.ui.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw_catsapi.model.CatBreed
import com.example.hw_catsapi.model.PagingItem
import com.example.hw_catsapi.repository.CatsRepository
import com.example.hw_catsapi.utils.mapToPage
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CatsBreedViewModel(private val repository: CatsRepository) : ViewModel() {

    private var page = 0
    private var isLoading = false
    private var currentPagingList: List<PagingItem<CatBreed>> = mutableListOf()
    private val _sharedFlow = MutableSharedFlow<List<PagingItem<CatBreed>>>(
        replay = 1, extraBufferCapacity = 0, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow: SharedFlow<List<PagingItem<CatBreed>>> = _sharedFlow.asSharedFlow()

    suspend fun fetchBreeds() {
        if (!isLoading && page <= LAST_PAGE) {
            isLoading = true
            try {
                currentPagingList = currentPagingList
                    .dropLast(1)
                    .plus(
                        repository.fetchBreeds(page)
                            .mapToPage()
                            .plus(PagingItem.Loading)
                    )
                if (page == LAST_PAGE) {
                    currentPagingList = currentPagingList.dropLast(1)
                }
                page++
            } catch (e: Throwable) {
                currentPagingList = currentPagingList
                    .dropLast(1)
                    .plus(PagingItem.Error(e))
            } finally {
                _sharedFlow.tryEmit(currentPagingList)
                isLoading = false
            }
        }
    }

    fun refresh() {
        page = 0
        currentPagingList = emptyList()
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
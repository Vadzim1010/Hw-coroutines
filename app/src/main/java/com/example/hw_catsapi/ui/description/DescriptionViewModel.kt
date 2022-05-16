package com.example.hw_catsapi.ui.description

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.repository.CatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DescriptionViewModel(private val repository: CatsRepository) : ViewModel() {


    suspend fun fetchDescription(breedId: String): Flow<CatDescription?>? = runCatching {
        repository.fetchDescription(breedId).getOrNull(0)
    }
        .onSuccess { }
        .map { flowOf(it) }
        .onFailure { /*do nothing */ }
        .getOrNull()
}


class DescriptionViewModelFactory(
    private val repository: CatsRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DescriptionViewModel(repository) as T
    }
}
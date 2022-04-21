package com.example.hw_catsapi.ui.breed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw_catsapi.model.Item
import com.example.hw_catsapi.repository.CatsRepository
import com.example.hw_catsapi.repository.Loading

class CatsBreedViewModel(private val repository: CatsRepository) : ViewModel() {

    fun getLoadingStatus(): LiveData<Loading> {
        return repository.getLoadingStatus()
    }

    fun fetchBreeds(): LiveData<List<Item>> {
        return repository.fetchBreeds()
    }

    fun refresh() {
        repository.refresh()
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
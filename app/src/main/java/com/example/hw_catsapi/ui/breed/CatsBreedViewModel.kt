package com.example.hw_catsapi.ui.breed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw_catsapi.model.LoadState
import com.example.hw_catsapi.repository.CatsRepository

class CatsBreedViewModel(private val repository: CatsRepository) : ViewModel() {

    fun fetchCatsBreeds(): LiveData<List<LoadState>> {
        return repository.fetchCatsBreeds()
    }
}


class CatsBreedViewModelFactory(private val repository: CatsRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatsBreedViewModel(repository) as T
    }
}
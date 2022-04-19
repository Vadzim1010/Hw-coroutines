package com.example.hw_catsapi.ui.breed

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw_catsapi.ui.model.CatBreed
import com.example.hw_catsapi.ui.repository.CatsRepository
import com.example.hw_catsapi.ui.retrofit.model.ApiCatsBreeds

class CatsBreedViewModel(private val repository: CatsRepository) : ViewModel() {

    fun fetchCatsBreeds(): LiveData<List<CatBreed>> {
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
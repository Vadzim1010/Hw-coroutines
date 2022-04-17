package com.example.hw_catsapi.ui.breed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw_catsapi.ui.repository.CatsRepository

class CatsBreedViewModel(private val repository: CatsRepository) : ViewModel() {
}


class CatsBreedViewModelFactory(private val repository: CatsRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CatsBreedViewModel(repository) as T
    }
}
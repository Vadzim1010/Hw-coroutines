package com.example.hw_catsapi.ui.description

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.repository.CatsRepository

class DescriptionViewModel(private val repository: CatsRepository) : ViewModel() {

    fun fetchDescription(id: String): LiveData<CatDescription?> {
        return repository.fetchDescription(id)
    }
}

class DescriptionViewModelFactory(
    private val repository: CatsRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DescriptionViewModel(repository) as T
    }

}
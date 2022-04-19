package com.example.hw_catsapi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hw_catsapi.model.LoadState
import com.example.hw_catsapi.retrofit.CatsApi
import com.example.hw_catsapi.retrofit.model.ApiCatsBreeds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsRepository(private val catsApi: CatsApi) {

    private val catsBreeds = MutableLiveData<List<LoadState>>()

    fun fetchCatsBreeds(): LiveData<List<LoadState>> {
        catsApi.getCatsBreed(1, 20)
            .enqueue(object : Callback<List<ApiCatsBreeds>> {
                override fun onResponse(
                    call: Call<List<ApiCatsBreeds>>,
                    response: Response<List<ApiCatsBreeds>>
                ) {
                    if (response.isSuccessful) {
                        catsBreeds.postValue(response.body()?.map {
                            LoadState.CatBreed(
                                it.id,
                                it.breed,
                                it.imageBreed?.imageBreedUrl
                            )
                        }?.plus(LoadState.Loading) ?: return)
                    }
                }

                override fun onFailure(call: Call<List<ApiCatsBreeds>>, t: Throwable) {
                    error("fetch cats Failure $t")
                }
            })
        return catsBreeds
    }
}
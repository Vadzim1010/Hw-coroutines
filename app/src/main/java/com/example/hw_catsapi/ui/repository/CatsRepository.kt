package com.example.hw_catsapi.ui.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hw_catsapi.ui.model.CatBreed
import com.example.hw_catsapi.ui.retrofit.CatsApi
import com.example.hw_catsapi.ui.retrofit.model.ApiCatsBreeds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsRepository(private val catsApi: CatsApi) {

    private val catsBreeds = MutableLiveData<List<CatBreed>>()

    fun fetchCatsBreeds(): LiveData<List<CatBreed>> {
        catsApi.getCatsBreed(1, 20)
            .enqueue(object : Callback<List<ApiCatsBreeds>> {
                override fun onResponse(
                    call: Call<List<ApiCatsBreeds>>,
                    response: Response<List<ApiCatsBreeds>>
                ) {
                    if (response.isSuccessful) {
                        catsBreeds.postValue(response.body()?.map {
                            CatBreed(
                                it.id,
                                it.breed,
                                it.imageBreed?.imageBreedUrl
                            )
                        })
                    }
                }

                override fun onFailure(call: Call<List<ApiCatsBreeds>>, t: Throwable) {
                    error("fetch cats Failure $t")
                }
            })
        return catsBreeds
    }
}
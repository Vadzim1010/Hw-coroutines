package com.example.hw_catsapi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hw_catsapi.model.Item
import com.example.hw_catsapi.retrofit.CatsApi
import com.example.hw_catsapi.retrofit.model.ApiCatsBreeds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsRepository(private val catsApi: CatsApi) {

    private val catsBreeds = MutableLiveData<List<Item>>()
    private val loadingStatus = MutableLiveData<Loading>()
    private var page = 0
    private var isLoading = false
    var limit = ""

    fun getLoadingStatus(): LiveData<Loading> {
        return loadingStatus
    }

    fun fetchFirstPage(): LiveData<List<Item>> {
        page = 0
        if (!isLoading) {
            isLoading = true
            loadingStatus.value = Loading.LOADING
            catsApi.getCatsBreed(page, 20)
                .enqueue(object : Callback<List<ApiCatsBreeds>> {
                    override fun onResponse(
                        call: Call<List<ApiCatsBreeds>>,
                        response: Response<List<ApiCatsBreeds>>
                    ) {
                        if (response.isSuccessful) {
                            limit = response.headers().get("Pagination-Count") ?: return
                            catsBreeds.postValue(response.body()?.map {
                                Item.CatBreed(
                                    it.id,
                                    it.breed,
                                    it.imageBreed?.imageBreedUrl
                                )
                            }?.plus(Item.Loading) ?: return)
                        }
                        isLoading = false
                        loadingStatus.value = Loading.NOT_LOADING
                    }

                    override fun onFailure(call: Call<List<ApiCatsBreeds>>, t: Throwable) {
                        catsBreeds.postValue(
                            listOf(
                                Item.Error(
                                    t.message ?: return
                                )
                            )
                        )
                        isLoading = false
                        loadingStatus.value = Loading.NOT_LOADING
                    }
                })
        }
        return catsBreeds
    }


    fun fetchNextPage(): LiveData<List<Item>> {
        val list = catsBreeds.value ?: emptyList()
        if (!isLoading && page < LAST_PAGE) {
            isLoading = true
            catsApi.getCatsBreed(++page, 20)
                .enqueue(object : Callback<List<ApiCatsBreeds>> {
                    override fun onResponse(
                        call: Call<List<ApiCatsBreeds>>,
                        response: Response<List<ApiCatsBreeds>>
                    ) {
                        if (response.isSuccessful) {
                            val responseList = response.body()?.map {
                                Item.CatBreed(
                                    it.id,
                                    it.breed,
                                    it.imageBreed?.imageBreedUrl
                                )
                            }?.plus(Item.Loading) ?: return
                            val resultList = list.dropLast(1) + responseList
                            if (page == LAST_PAGE) {
                                catsBreeds.postValue(resultList.dropLast(1))
                            } else {
                                catsBreeds.postValue(resultList)
                            }
                        }
                        isLoading = false
                    }

                    override fun onFailure(call: Call<List<ApiCatsBreeds>>, t: Throwable) {
                        val resultList = list.dropLast(1).plus(
                            Item.Error(
                                t.message ?: return
                            )
                        )
                        catsBreeds.postValue(resultList)
                        isLoading = false
                    }
                })
        }
        return catsBreeds
    }

    companion object {
        private const val LAST_PAGE = 3
    }
}

enum class Loading {
    NOT_LOADING, LOADING
}
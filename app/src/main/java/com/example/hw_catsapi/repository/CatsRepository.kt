package com.example.hw_catsapi.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.hw_catsapi.model.CatDescription
import com.example.hw_catsapi.model.Item
import com.example.hw_catsapi.retrofit.CatsApi
import com.example.hw_catsapi.retrofit.model.ApiCatsBreeds
import com.example.hw_catsapi.retrofit.model.ApiDescription
import com.example.hw_catsapi.utils.mapBreeds
import com.example.hw_catsapi.utils.mapDescription
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatsRepository(private val catsApi: CatsApi) {

    private val catsBreeds = MutableLiveData<List<Item>>()
    private val loadingStatus = MutableLiveData<Loading>()
    private var page = 0
    private var isLoading = false
    private var description = MutableLiveData<CatDescription?>()


    fun fetchDescription(id: String): LiveData<CatDescription?> {
        catsApi.getBreedById(id)
            .enqueue(object : Callback<List<ApiDescription>> {
                override fun onResponse(
                    call: Call<List<ApiDescription>>,
                    response: Response<List<ApiDescription>>
                ) {
                    if (response.isSuccessful) {
                        val descriptionList = response.body()?.mapDescription()
                        description.postValue(descriptionList?.getOrNull(0))
                    }
                }

                override fun onFailure(call: Call<List<ApiDescription>>, t: Throwable) {
                    //do nothing
                }
            })
        return description
    }

    fun getLoadingStatus(): LiveData<Loading> {
        return loadingStatus
    }


    fun fetchBreeds(): LiveData<List<Item>> {
        val currentList = catsBreeds.value ?: emptyList()
        if (!isLoading && page < LAST_PAGE) {
            isLoading = true
            loadingStatus.value = Loading.LOADING
            catsApi.getCatsBreed(page++, 20)
                .enqueue(object : Callback<List<ApiCatsBreeds>> {
                    override fun onResponse(
                        call: Call<List<ApiCatsBreeds>>,
                        response: Response<List<ApiCatsBreeds>>
                    ) {
                        if (response.isSuccessful) {
                            Log.i("TAG", page.toString())
                            val responseList = response.body()
                                ?.mapBreeds()
                                ?.plus(Item.Loading) ?: return

                            val resultList = currentList.dropLast(1) + responseList

                            if (page == LAST_PAGE) {
                                catsBreeds.postValue(resultList.dropLast(1))
                            } else {
                                catsBreeds.postValue(resultList)
                            }
                        }
                        isLoading = false
                        loadingStatus.value = Loading.NOT_LOADING
                    }

                    override fun onFailure(call: Call<List<ApiCatsBreeds>>, t: Throwable) {
                        val resultList = currentList.dropLast(1).plus(
                            Item.Error(
                                t.message ?: return
                            )
                        )
                        catsBreeds.postValue(resultList)
                        isLoading = false
                        loadingStatus.value = Loading.NOT_LOADING
                    }
                })
        }
        return catsBreeds
    }

    fun refresh() {
        page = 0
        catsBreeds.value = emptyList()
        fetchBreeds()
    }

    companion object {
        private const val LAST_PAGE = 4
    }
}

enum class Loading {
    NOT_LOADING, LOADING
}

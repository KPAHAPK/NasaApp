package com.example.nasaapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.repository.PODData
import com.example.nasaapp.repository.PODRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "PODViewModel"

class PODViewModel() : ViewModel() {
    private val liveDataToObserver: MutableLiveData<PODData> = MutableLiveData()
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()

    fun getLiveData(): LiveData<PODData> {
        return liveDataToObserver
    }

    fun sendServerRequest() {
        liveDataToObserver.postValue(PODData.Loading)
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            error("API ключ пустой")
        } else {
            retrofitImpl.getRetrofitImpl().getPOD(apiKey).enqueue(
                object : Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                liveDataToObserver.postValue(PODData.Success(it))
                                Log.d(TAG, "Запрос выполнен успешно")
                            }

                        } else {
                            Log.d(TAG, "My API Key: $apiKey")
                            error("Запрос отклонен")
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                       // TODO("HW")
                    }

                }

            )
        }
    }
}
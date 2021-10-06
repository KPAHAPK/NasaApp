package com.example.nasaapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.R
import com.example.nasaapp.repository.responsedata.PODData
import com.example.nasaapp.repository.responsedata.PODServerResponseData
import com.example.nasaapp.repository.retrofit.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "PODViewModel"

class PODViewModel(application: Application) : AndroidViewModel(application) {
    private val liveDataToObserver: MutableLiveData<PODData> = MutableLiveData()
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()

    fun getLiveData(): LiveData<PODData> {
        return liveDataToObserver
    }

    fun sendServerRequestPOD(dayOffset: Int = 0) {
        liveDataToObserver.postValue(PODData.Loading)
        val apiKey = BuildConfig.NASA_API_KEY

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, dayOffset)
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = simpleDateFormat.format(calendar.time)

        if (apiKey.isBlank()) {
            error(R.string.api_key_is_blank)
        } else {
            retrofitImpl.getPOD(date, apiKey, PODCallBack)
        }
    }

    private val PODCallBack = object : Callback<PODServerResponseData> {
        override fun onResponse(
            call: Call<PODServerResponseData>,
            response: Response<PODServerResponseData>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveDataToObserver.postValue(PODData.Success(it))
                }

            } else {
                val code = response.code()
                val message = response.message()
                liveDataToObserver.value =
                    PODData.Error(Throwable("Error $code: $message"))
                Toast.makeText(
                    getApplication(),
                    "Error $code: $message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
            liveDataToObserver.value = PODData.Error(Throwable(t))
        }

    }
}
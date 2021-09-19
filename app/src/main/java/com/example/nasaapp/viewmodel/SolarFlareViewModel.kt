package com.example.nasaapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.nasaapp.BuildConfig
import com.example.nasaapp.R
import com.example.nasaapp.repository.responsedata.SolarFlareData
import com.example.nasaapp.repository.responsedata.SolarFlareServerResponseData
import com.example.nasaapp.repository.retrofit.RetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class SolarFlareViewModel(application: Application) : AndroidViewModel(application) {
    private val liveDataToObserve: MutableLiveData<SolarFlareData> = MutableLiveData()
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()

    fun getLiveData(): LiveData<SolarFlareData> {
        return liveDataToObserve
    }

    fun sendServerRequestSolarFlare() {
        liveDataToObserve.postValue(SolarFlareData.Loading)
        val api = BuildConfig.NASA_API_KEY

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val pattern = "yyyy-MM-dd"
        val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = simpleDateFormat.format(calendar.time)

        if (api.isBlank()) {
            error(R.string.api_key_is_blank)
        } else {
            retrofitImpl.getLastSolarFlare(date, api, LastSolarFlareCallBack)
        }
    }

    private val LastSolarFlareCallBack = object : Callback<List<SolarFlareServerResponseData>> {
        override fun onResponse(
            call: Call<List<SolarFlareServerResponseData>>,
            response: Response<List<SolarFlareServerResponseData>>
        ) {
            if (response.isSuccessful) {
                response.body()?.let {
                    liveDataToObserve.postValue(SolarFlareData.Success(it))
                }
            } else {
                val code = response.code()
                val message = response.message()
                liveDataToObserve.value =
                    SolarFlareData.Error(Throwable("Error $code: $message"))
                Toast.makeText(
                    getApplication(),
                    "Error $code: $message",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        override fun onFailure(call: Call<List<SolarFlareServerResponseData>>, t: Throwable) {
            liveDataToObserve.value = SolarFlareData.Error(Throwable(t))
        }

    }
}
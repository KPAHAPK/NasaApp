package com.example.nasaapp.repository.responsedata

sealed class PODData {
    data class Success(val serverResponseData: PODServerResponseData) : PODData()
    data class Error(val throwable: Throwable) : PODData()
    object Loading : PODData()
}

sealed class SolarFlareData {
    data class Success(val serverResponseData: List<SolarFlareServerResponseData>) : PODData()
    data class Error(val throwable: Throwable) : PODData()
    object Loading : PODData()
}
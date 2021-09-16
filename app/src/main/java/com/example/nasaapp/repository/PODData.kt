package com.example.nasaapp.repository

import com.example.nasaapp.viewmodel.PODServerResponseData

sealed class PODData {
    data class Success(val serverResponseData: PODServerResponseData): PODData()
    data class Error(val throwable: Throwable): PODData()
    object Loading : PODData()
}
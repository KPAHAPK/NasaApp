package com.example.nasaapp.repository.responsedata

data class SolarFlareServerResponseData(
    val flrID: String,
    val beginTime: String,
    val peakTime: String,
    val endTime: String? = null,
    val classType: String,
    val sourceLocation: String,
    val activeRegionNum: Long,
    val link: String
)
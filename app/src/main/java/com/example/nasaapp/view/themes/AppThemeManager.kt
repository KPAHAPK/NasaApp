package com.example.nasaapp.view.themes

import androidx.annotation.StyleRes
import com.example.nasaapp.R

enum class AppThemeManager(@StyleRes val theme: Int, val key: String) {
    BLUE(R.style.Theme_NasaApp_myBlueTheme, "BlueTheme"),
    LIME(R.style.Theme_NasaApp_myLimeTheme, "LimeTheme"),
    FUCHSIA(R.style.Theme_NasaApp_myFuchsiaTheme, "FuchsiaTheme"),
    MAROON(R.style.Theme_NasaApp_myMaroonTheme, "MaroonTheme"),
    OLIVE(R.style.Theme_NasaApp_myOliveTheme, "OliveTheme"),
    NAVY(R.style.Theme_NasaApp_myNavyTheme, "NavyTheme"),
    AQUA(R.style.Theme_NasaApp_myAquaTheme, "AquaTheme"),
    DEFAULT(R.style.Theme_NasaApp, "DefaultTheme");

}
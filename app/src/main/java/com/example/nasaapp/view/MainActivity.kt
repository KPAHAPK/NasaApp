package com.example.nasaapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.R
import com.example.nasaapp.view.picture.PODFragment
import com.example.nasaapp.view.themes.AppThemeStorage


class MainActivity : AppCompatActivity() {

//    private lateinit var appThemeStorage: AppThemeStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appThemeStorage = AppThemeStorage(this)
        setTheme(appThemeStorage.getTheme().theme)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, SettingsFragment.newInstance())
//                .commitNow()

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, PODFragment.newInstance())
                .commitNow()
        }
    }
}
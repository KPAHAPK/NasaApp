package com.example.nasaapp.view.themes

import android.content.Context


private const val KEY = "ThemeKey"

class AppThemeStorage(private val context: Context) {

    private val sPref = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)

    fun setTheme(appThemeManager: AppThemeManager) {
        sPref.edit()
            .putString(KEY, appThemeManager.key)
            .apply()
    }

    fun getTheme(): AppThemeManager {
        val sPrefKey = sPref.getString(KEY, AppThemeManager.AQUA.key)

        for (appTheme in AppThemeManager.values()) {
            if (appTheme.key == sPrefKey) {
                return appTheme
            }
        }
        throw IllegalStateException("Тема не найдена")
    }
}
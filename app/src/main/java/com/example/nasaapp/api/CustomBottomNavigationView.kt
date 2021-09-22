package com.example.nasaapp.api

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val MAX_ITEM_COUNT = 10
    }
}
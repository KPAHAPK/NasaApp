package com.example.nasaapp.api

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ActivityApiBottomBinding
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView

class ApiBottomActivity : AppCompatActivity() {

    lateinit var binding: ActivityApiBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBottomBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavigationView.apply {
            labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED

            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.bottom_view_earth -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.containerApi, LastSolarFlareFragment.newInstance())
                            .commit()
                        true
                    }
                    R.id.bottom_view_mars -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.containerApi, MarsFragment.newInstance())
                            .commit()
                        true
                    }
                    R.id.bottom_view_system -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.containerApi, SystemFragment.newInstance())
                            .commit()
                        true
                    }
                    else -> false
                }
            }

            selectedItemId = R.id.bottom_view_mars

            setOnItemReselectedListener { item ->
                when (item.itemId) {
                    R.id.bottom_view_earth -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.containerApi, LastSolarFlareFragment.newInstance())
                            .commit()
                    }
                    R.id.bottom_view_mars -> {
                        //Item tapped
                    }
                    R.id.bottom_view_system -> {
                        //Item tapped
                    }
                }
            }

            getOrCreateBadge(R.id.bottom_view_earth).apply {
                number = 10000000
                badgeGravity = BadgeDrawable.BOTTOM_START
                maxCharacterCount = 10
            }

            menu.getItem(0).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tooltipText = "Это Земля"
                }
            }

        }
    }
}
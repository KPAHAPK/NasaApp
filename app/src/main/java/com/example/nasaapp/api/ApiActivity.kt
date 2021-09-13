package com.example.nasaapp.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.databinding.ActivityApiBinding


class ApiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.apply {
            adapter = ViewPagerAdapter(supportFragmentManager)
        }

        binding.tabLayout.apply {
            setupWithViewPager(binding.viewPager)
            getTabAt(0)?.setIcon(android.R.drawable.alert_dark_frame)
            getTabAt(1)?.setIcon(android.R.drawable.btn_default)
            getTabAt(2)?.setIcon(android.R.drawable.arrow_down_float)
        }

    }
}
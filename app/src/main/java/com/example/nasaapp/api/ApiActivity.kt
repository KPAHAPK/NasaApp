package com.example.nasaapp.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.databinding.ActivityApiBinding
import com.google.android.material.tabs.TabLayoutMediator


class ApiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.apply {
            adapter = ViewPagerAdapter(this@ApiActivity)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "OBJECT Mars"
                1 -> tab.text = "OBJECT Earth"
                2 -> tab.text = "OBJECT System"
            }
        }.attach()


    }
}
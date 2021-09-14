package com.example.nasaapp.api

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ActivityApiBinding
import com.google.android.material.tabs.TabLayout


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
//            getTabAt(0)?.setIcon(android.R.drawable.alert_dark_frame)
//            getTabAt(1)?.setIcon(android.R.drawable.btn_default)
//            getTabAt(2)?.setIcon(android.R.drawable.arrow_down_float)

            val frameLayoutInflate = layoutInflater.inflate(R.layout.activity_api_tablayout_item, null, false)

            iterator().apply {
                for (tab in this){
                    if (this.hasNext()){
                    }
                }
            }

            getTabAt(0)?.customView = layoutInflater.inflate(R.layout.activity_api_tablayout_item, null, false)


        }

    }
}
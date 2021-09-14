package com.example.nasaapp.api

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.nasaapp.R

class ViewPagerAdapter(private val fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    private var fragments = arrayOf(EarthFragment(), MarsFragment(), SystemFragment())

    override fun getCount() = fragments.size

//    override fun getPageTitle(position: Int): CharSequence? {
//        return when(position){
//            0 -> "Earth"
//            1 -> "Mars"
//            2 -> "System"
//            else -> ""
//        }
//    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}

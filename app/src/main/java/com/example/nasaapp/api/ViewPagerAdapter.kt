package com.example.nasaapp.api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(private val fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var fragments = arrayOf(EarthFragment(), MarsFragment(), SystemFragment())

//    override fun getCount() = fragments.size

//    override fun getPageTitle(position: Int): CharSequence? {
//        return when(position){
//            0 -> "Earth"
//            1 -> "Mars"
//            2 -> "System"
//            else -> ""
//        }
//    }
//
//    override fun getItem(position: Int): Fragment {
//        return fragments[position]
//    }

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}

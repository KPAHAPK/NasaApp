package com.example.nasaapp.view.picture

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nasaapp.R
import com.example.nasaapp.api.ApiActivity
import com.example.nasaapp.api.ApiBottomActivity
import com.example.nasaapp.databinding.BottomNavigationLayoutBinding
import com.example.nasaapp.viewLifeCycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerPODFragment : BottomSheetDialogFragment() {

    private var binding: BottomNavigationLayoutBinding by viewLifeCycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomNavigationLayoutBinding.inflate(inflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_one -> {
                    val intent = Intent(requireContext(), ApiActivity::class.java)
                    val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(),null, "" )
                    startActivity(intent, options.toBundle())
                }
                R.id.navigation_two -> {
                    val intent = Intent(requireContext(), ApiBottomActivity::class.java)
                    startActivity(intent)
                }
            }
            dismiss()
            true
        }
    }

    companion object {
        fun newInstance() = BottomNavigationDrawerPODFragment()

    }
}
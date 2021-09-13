package com.example.nasaapp.view.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nasaapp.R
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
                    Toast.makeText(requireContext(), "На экран 1", Toast.LENGTH_SHORT).show()
                }
                R.id.navigation_two -> {
                    Toast.makeText(requireContext(), "На экран 2", Toast.LENGTH_SHORT).show()
                }

            }
            true
        }
    }

    companion object {
        fun newInstance() = BottomNavigationDrawerPODFragment()

    }
}
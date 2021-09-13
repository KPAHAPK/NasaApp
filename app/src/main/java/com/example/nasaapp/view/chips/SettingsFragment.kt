package com.example.nasaapp.view.chips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FragmentSettingsBinding
import com.example.nasaapp.view.picture.BottomSheetThemeListFragment
import com.example.nasaapp.viewLifeCycle

class SettingsFragment : Fragment() {

    private var binding: FragmentSettingsBinding by viewLifeCycle()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.includedChips.chipGroup.setOnCheckedChangeListener { _, position ->
            Toast.makeText(context, "Click $position", Toast.LENGTH_SHORT).show()
        }
        binding.includedChips.chipClose.setOnCloseIconClickListener {
            Toast.makeText(context, "Click on chipWithClose", Toast.LENGTH_SHORT).show()
        }

        binding.myBottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.theme_changer -> {
                    activity?.let {
                        BottomSheetThemeListFragment().show(it.supportFragmentManager, "")
                    }
                    true
                }
                else -> false
            }
        }

    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
package com.example.nasaapp.view.picture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.nasaapp.databinding.BottomSheetThemeListBinding
import com.example.nasaapp.view.themes.AppThemeManager
import com.example.nasaapp.view.themes.AppThemeStorage
import com.example.nasaapp.viewLifeCycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetThemeListFragment : BottomSheetDialogFragment() {

    private lateinit var appThemeStorage: AppThemeStorage

    var binding: BottomSheetThemeListBinding by viewLifeCycle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetThemeListBinding.inflate(inflater)
        appThemeStorage = AppThemeStorage(requireContext())
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setThemeListOnClickListeners()
    }

    private fun setThemeListOnClickListeners() {
        binding.apply {
            bindClickOnVIewWithTheme(blueTheme, AppThemeManager.BLUE)
            bindClickOnVIewWithTheme(limeTheme, AppThemeManager.LIME)
            bindClickOnVIewWithTheme(fuchsiaTheme, AppThemeManager.FUCHSIA)
            bindClickOnVIewWithTheme(maroonTheme, AppThemeManager.MAROON)
            bindClickOnVIewWithTheme(oliveTheme, AppThemeManager.OLIVE)
            bindClickOnVIewWithTheme(navyTheme, AppThemeManager.NAVY)
            bindClickOnVIewWithTheme(aquaTheme, AppThemeManager.AQUA)
            bindClickOnVIewWithTheme(defaultTheme, AppThemeManager.DEFAULT)
        }
    }

    private fun bindClickOnVIewWithTheme(textView: TextView, theme: AppThemeManager) {
        textView.setOnClickListener {
            applyTheme(theme)
        }
    }

    private fun applyTheme(theme: AppThemeManager) {
        appThemeStorage.setTheme(theme)
        dismiss()
        activity?.recreate()
    }

    companion object {
        fun newInstance() = BottomSheetDialogFragment()
    }

}
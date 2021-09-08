package com.example.nasaapp.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FragmentMainBinding
import com.example.nasaapp.repository.PODData
import com.example.nasaapp.view.MainActivity
import com.example.nasaapp.view.chips.ChipsFragment
import com.example.nasaapp.viewLifeCycle
import com.example.nasaapp.viewmodel.PODViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

class PODFragment : Fragment() {
    private var binding: FragmentMainBinding by viewLifeCycle()

    private val viewModelPOD: PODViewModel by lazy {
        ViewModelProvider(this)[PODViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        val view = binding.root
        setAppBar()
        return view
    }

    private var isMain = true
    private fun setAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelPOD.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        viewModelPOD.sendServerRequest()

        binding.inputLayout.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("http://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            }
            startActivity(intent)
        }
        setBottomSheetBehaviour(binding.includedBottomSheet.bottomSheetContainer)
        binding.chipImageChooser.setOnCheckedChangeListener { _, position ->

        }
    }

    private fun setBottomSheetBehaviour(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        /*  bottomSheetBehavior.addBottomSheetCallback(object :
              BottomSheetBehavior.BottomSheetCallback() {
              override fun onStateChanged(bottomSheet: View, newState: Int) {
                  when (newState) {
                      BottomSheetBehavior.STATE_DRAGGING -> TODO("not implemented")
                      BottomSheetBehavior.STATE_COLLAPSED -> TODO("not implemented")
                      BottomSheetBehavior.STATE_EXPANDED -> TODO("not implemented")
                      BottomSheetBehavior.STATE_HALF_EXPANDED -> TODO("not implemented")
                      BottomSheetBehavior.STATE_HIDDEN -> TODO("not implemented")
                      BottomSheetBehavior.STATE_SETTLING -> TODO("not implemented")
                  }
              }

              override fun onSlide(bottomSheet: View, slideOffset: Float) {
                  TODO("not implemented")
              }
          })*/
    }

    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Error -> {//TODO HW
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
            is PODData.Loading -> {
                Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
            }
            is PODData.Success -> {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                binding.imagePictureOfTheDate.load(data.serverResponseData.hdurl) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> {
                Toast.makeText(requireContext(), "Favourite", Toast.LENGTH_SHORT).show()
            }
            R.id.app_bar_settings -> {
                Toast.makeText(requireContext(), "Settings", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ChipsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            R.id.app_bar_search -> {
                Toast.makeText(requireContext(), "Search", Toast.LENGTH_SHORT).show()
            }
            android.R.id.home -> {
                Toast.makeText(requireContext(), "Home", Toast.LENGTH_SHORT).show()
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return true
    }

    companion object {
        fun newInstance() = PODFragment()
    }


}


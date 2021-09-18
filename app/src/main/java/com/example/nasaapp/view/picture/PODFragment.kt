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
import com.example.nasaapp.repository.responsedata.PODData
import com.example.nasaapp.view.MainActivity
import com.example.nasaapp.view.chips.SettingsFragment
import com.example.nasaapp.viewLifeCycle
import com.example.nasaapp.viewmodel.PODViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar

private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


class PODFragment : Fragment() {
    private var binding: FragmentMainBinding by viewLifeCycle()

    private val viewModelPOD: PODViewModel by lazy {
        ViewModelProvider(this)[PODViewModel::class.java]
    }

    private var PODDayOffset: Int = 0

    private var copyright: String? = null
    private var date: String? = null
    private var explanation: String? = null
    private var mediaType: String? = null
    private var title: String? = null
    private var url: String? = null
    private var hdurl: String? = null


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
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_back_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_plus_fab
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelPOD.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }

        viewModelPOD.sendServerRequest(PODDayOffset)


        binding.inputLayout.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse(
                        getString(
                            R.string.baseWikiURL,
                            binding.inputEditText.text.toString()
                        )
                    )
            }
            startActivity(intent)
        }
        setBottomSheetBehaviour(binding.includedBottomSheet.bottomSheetContainer)
        setChipPODListener()

    }

    private fun setChipPODListener() {
        var lastCheckedId = View.NO_ID
        binding.chipImageChooser.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == View.NO_ID) {
                group.check(lastCheckedId)
                return@setOnCheckedChangeListener
            }
            lastCheckedId = checkedId

            val chip: Chip? = group.findViewById(lastCheckedId)
            chip?.let {
                viewModelPOD.apply {
                    when (chip) {
                        binding.chipImageToday -> {
                            PODDayOffset = 0
                        }
                        binding.chipImageYesterday -> {
                            PODDayOffset = -1
                        }
                        binding.chipImageBeforeYesterday -> {
                            PODDayOffset = -2
                        }
                    }
                    sendServerRequest(PODDayOffset)
                }
            }
        }

        binding.chipImageHd.setOnCheckedChangeListener { _, isChecked ->
            binding.imagePictureOfTheDate.apply {
                when (isChecked) {
                    true -> load(hdurl)
                    false -> load(url)
                }
            }
        }

    }

    private fun setBottomSheetBehaviour(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
            is PODData.Error -> {
                val errorSnackbar =
                    Snackbar.make(binding.root, R.string.retry_request, Snackbar.LENGTH_INDEFINITE)
                errorSnackbar.setAction(R.string.snackbar_retry_message) {
                    viewModelPOD.sendServerRequest(PODDayOffset)
                }
                errorSnackbar.show()
            }
            is PODData.Loading -> {
                binding.imagePictureOfTheDate.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is PODData.Success -> {
                copyright = data.serverResponseData.copyright.toString()
                date = data.serverResponseData.date.toString()
                explanation = data.serverResponseData.explanation.toString()
                mediaType = data.serverResponseData.mediaType.toString()
                title = data.serverResponseData.title.toString()
                url = data.serverResponseData.url.toString()
                hdurl = data.serverResponseData.hdurl.toString()
                updateUI()
            }
        }
    }

    private fun updateUI() {
        binding.imagePictureOfTheDate.apply {
            if (binding.chipImageHd.isChecked) {
                load(url) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                }
            } else {
                load(hdurl) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                }
            }
        }
        binding.includedBottomSheet.bottomSheetDescriptionHeader.text = title
        binding.includedBottomSheet.bottomSheetDescription.text = explanation

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> {
                Toast.makeText(requireContext(), R.string.favourite, Toast.LENGTH_SHORT).show()
            }
            R.id.app_bar_settings -> {
                Toast.makeText(requireContext(), R.string.settings, Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance())
                    .addToBackStack("")
                    .commit()
            }
            android.R.id.home -> {
                Toast.makeText(requireContext(), R.string.home, Toast.LENGTH_SHORT).show()
                activity?.let {
                    BottomNavigationDrawerPODFragment().show(it.supportFragmentManager, "")
                }
            }
        }
        return true
    }

    companion object {
        fun newInstance() = PODFragment()
    }


}


package com.example.nasaapp.view.picture

import android.animation.AnimatorInflater
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FragmentMainStartBinding
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
    private var binding: FragmentMainStartBinding by viewLifeCycle()

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
    ): View {
        binding = FragmentMainStartBinding.inflate(inflater)
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
        viewModelPOD.sendServerRequestPOD(PODDayOffset)

        setWikiSearchOnClick()
        setBottomSheetBehaviour(binding.includedBottomSheet.bottomSheetContainer)

        setChipsPODListener()

        setImageScaleAnimation(binding.imagePictureOfTheDate)
    }

    private fun setImageScaleAnimation(imageView: ImageView) {
        var isExpanded = false
        imageView.setOnClickListener {
            isExpanded = !isExpanded
            val changeBounds = ChangeBounds()
            changeBounds.resizeClip = true

            val set = TransitionSet()
                .addTransition(changeBounds)
                .addTransition(ChangeImageTransform())
            set.interpolator = AnticipateOvershootInterpolator(2.0f)

            TransitionManager.beginDelayedTransition(binding.mainContainer, set)

            val param: ViewGroup.LayoutParams = it.layoutParams
            param.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            it.layoutParams = param
            binding.imagePictureOfTheDate.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
    }

    private fun setWikiSearchOnClick() {
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
    }

    private fun setBottomSheetBehaviour(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setChipsPODListener() {
        binding.chipImageChooser.setOnCheckedChangeListener { group, checkedId ->
            var lastCheckedId = View.NO_ID
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
                    setRotateViewAnimation(chip)
                    sendServerRequestPOD(PODDayOffset)
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

    private fun setRotateViewAnimation(view: View) {
        val viewAnimator =
            AnimatorInflater.loadAnimator(requireContext(), R.animator.chip_animator)
        val scale = requireContext().resources.displayMetrics.density
        view.cameraDistance = 8000 * scale
        viewAnimator.setTarget(view)
        viewAnimator.start()
    }


    private fun renderData(data: PODData) {
        when (data) {
            is PODData.Error -> {
                val errorSnackbar =
                    Snackbar.make(binding.root, R.string.retry_request, Snackbar.LENGTH_INDEFINITE)
                errorSnackbar.setAction(R.string.snackbar_retry_message) {
                    viewModelPOD.sendServerRequestPOD(PODDayOffset)
                }
                errorSnackbar.show()
            }
            is PODData.Loading -> {
                binding.playVideoOfTheDay.visibility = View.INVISIBLE

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
        setMedia()
        binding.includedBottomSheet.bottomSheetDescriptionHeader.text = title
        binding.includedBottomSheet.bottomSheetDescription.text = explanation
        binding.includedBottomSheet.bottomSheetCopyright.text = copyright

    }

    private fun setMedia() {
        when (mediaType) {
            "image" -> setUrlOrHdurlImage()
            "video" -> {
                val idVideo = url?.substringAfterLast("/")?.substringBeforeLast("?")
                binding.imagePictureOfTheDate.load(Uri.parse("https://img.youtube.com/vi/$idVideo/1.jpg"))
                binding.playVideoOfTheDay.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setUrlOrHdurlImage() {
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
                val settingFragment = requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_in, R.anim.fragment_out)
                    .replace(R.id.main_container, SettingsFragment.newInstance())
                    .addToBackStack("")
                settingFragment.commit()
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

    private fun setStartActivityAnimation() {
        val slide = Slide()
        slide.slideEdge = Gravity.START
        slide.duration = 400
        slide.interpolator = (DecelerateInterpolator())
        val set = TransitionSet()
            .addTransition(slide)
            .clone()
        TransitionManager.beginDelayedTransition(binding.root, set)
    }

    companion object {
        fun newInstance() = PODFragment()
    }


}


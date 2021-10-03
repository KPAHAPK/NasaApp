package com.example.nasaapp.view.picture

import android.animation.AnimatorInflater
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BulletSpan
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
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
import kotlinx.android.synthetic.main.bottom_sheet_theme_list.view.*

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
        binding.includedBottomSheet.includeLayoutTv.textView.apply {
            text = explanation
            typeface = Typeface.createFromAsset(requireActivity().assets, "font1.ttf")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                typeface = resources.getFont(R.font.font2)
            }
            val text2 = "My text \nbullet one \nbullet two"
            val spannapble = SpannableStringBuilder(text2)
            spannapble.setSpan(
                BulletSpan(
                    BulletSpan.STANDARD_GAP_WIDTH,
                    resources.getColor(R.color.colorAccent, activity?.theme)
                ),
                text2.indexOf("\n", text2.indexOf("\n")) +1,
                text2.indexOf("\n", text2.indexOf("\n") + 1),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannapble.setSpan(
                BulletSpan(
                    BulletSpan.STANDARD_GAP_WIDTH,
                    resources.getColor(R.color.colorAccent, activity?.theme)
                ),
                text2.indexOf("\n", text2.indexOf("\n") + 1) + 1,
                text2.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            this.text = spannapble

        }
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
                Toast.makeText(requireContext(), R.string.settings, Toast.LENGTH_SHORT).show()
                val settingFragment = requireActivity().supportFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_in_animation, R.anim.fragment_out)
                    .replace(R.id.main_container, SettingsFragment.newInstance())
                    .addToBackStack("")
                settingFragment.commit()
            }
            android.R.id.home -> {
                Toast.makeText(requireContext(), R.string.home, Toast.LENGTH_SHORT).show()
                activity?.let {
                    val homeFragment = it.supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(0, R.anim.fragment_out)

                    BottomNavigationDrawerPODFragment().show(homeFragment, "")
                }
            }
        }
        return true
    }

    companion object {
        fun newInstance() = PODFragment()
    }


}


package com.example.nasaapp.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FragmentMainBinding
import com.example.nasaapp.repository.PODData
import com.example.nasaapp.viewLifeCycle
import com.example.nasaapp.viewmodel.PODViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

class PODFragment: Fragment() {
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelPOD.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        viewModelPOD.sendServerRequest()

        binding.inputLayout.setEndIconOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("http://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            }
            startActivity(intent)
        }
        setBottomSheetBehaviour(binding.includedBottomSheet.bottomSheetContainer)
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
                binding.imagePictureOfTheDate.load(data.serverResponseData.url) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
        }
    }

    private fun setBottomSheetBehaviour(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object :
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
        })
    }

    companion object{
        fun newInstance() = PODFragment()
    }


}


package com.example.nasaapp.view.picture

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FragmentMainBinding
import com.example.nasaapp.repository.PODData
import com.example.nasaapp.viewLifeCycle
import com.example.nasaapp.viewmodel.PODViewModel

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

    companion object{
        fun newInstance() = PODFragment()
    }


}


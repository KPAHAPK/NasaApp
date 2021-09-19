package com.example.nasaapp.api

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FragmentLastSolarFlareBinding
import com.example.nasaapp.repository.responsedata.SolarFlareData
import com.example.nasaapp.viewLifeCycle
import com.example.nasaapp.viewmodel.SolarFlareViewModel

class LastSolarFlareFragment : Fragment() {

    private var binding: FragmentLastSolarFlareBinding by viewLifeCycle()

    private var beginTime : String? = null
    private var peakTime  : String? = null
    private var endTime : String? = null
    private var sourceLocation : String? = null

    private val viewModelSolarFlare: SolarFlareViewModel by lazy {
        ViewModelProvider(this).get(SolarFlareViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLastSolarFlareBinding.inflate(inflater)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelSolarFlare.getLiveData().observe(viewLifecycleOwner) {
            renderData(it)
        }
        viewModelSolarFlare.sendServerRequestSolarFlare()


    }

    private fun updateUI() {
        binding.solarFlareBeginTime.text = getString(R.string.solar_flare_begin_time, beginTime)
        binding.solarFlarePeakTime.text = getString(R.string.solar_flare_peak_time, peakTime)
        binding.solarFlareEndTime.text = getString(R.string.solar_flare_end_time, endTime)
        binding.solarFlareLocation.text = getString(R.string.solar_flare_location, sourceLocation)
    }

    private fun renderData(data: SolarFlareData){
        when(data){
            is SolarFlareData.Loading -> Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
            is SolarFlareData.Error -> {}
            is SolarFlareData.Success -> {
                beginTime = data.serverResponseData.last().beginTime
                peakTime = data.serverResponseData.last().peakTime
                endTime = data.serverResponseData.last().endTime
                sourceLocation = data.serverResponseData.last().sourceLocation
                updateUI()
            }
        }
    }

    companion object {
        fun newInstance() = LastSolarFlareFragment()
    }
}
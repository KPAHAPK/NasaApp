package com.example.nasaapp.view.picture

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.nasaapp.R
import com.example.nasaapp.api.ApiActivity
import com.example.nasaapp.api.ApiBottomActivity
import com.example.nasaapp.databinding.BottomNavigationLayoutBinding
import com.example.nasaapp.view.recycler.myRecycler.MyRecyclerActivity
import com.example.nasaapp.view.recycler.recycler.RecyclerActivity
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
                    val intent = Intent(requireContext(), ApiBottomActivity::class.java)
//                  val options = ActivityOptions.makeSceneTransitionAnimation(requireActivity(),
//                  Pair.create(binding.navigationView, "screen2"))
                    val viewSource = view.findViewById<View>(R.id.navigation_one)
                    val coordinateX = viewSource.x.toInt()
                    val coordinateY = viewSource.y.toInt()
                    val options = ActivityOptionsCompat.makeScaleUpAnimation(
                        viewSource,
                        coordinateX,
                        coordinateY,
                        10000,
                        10000
                    )
                    startActivity(intent, options.toBundle())
                }
                R.id.navigation_two -> {
                    val intent = Intent(requireContext(), ApiActivity::class.java)
                    val viewSource = binding.navigationView
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        requireActivity(),
                        Pair(viewSource, "screen2")
                    )
                    startActivity(intent, options.toBundle())
                }
                R.id.navigation_three -> activity?.let {
                    startActivity(
                        Intent(
                            it,
                            RecyclerActivity::class.java
                        )
                    )
                }
                R.id.navigation_four -> activity?.let {
                    startActivity(
                        Intent(
                            it,
                            MyRecyclerActivity::class.java
                        )
                    )
                }
            }
            dismiss()
            true
        }
    }

    companion object {
        fun newInstance() = BottomNavigationDrawerPODFragment()

    }
}
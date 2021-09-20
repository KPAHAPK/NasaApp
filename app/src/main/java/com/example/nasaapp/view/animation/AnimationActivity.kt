package com.example.nasaapp.view.animation

import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.databinding.ActivityAnimationBinding

class AnimationActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAnimationBinding

    private var textIsVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener{
            TransitionManager.beginDelayedTransition(binding.transitionsContainer, Slide(Gravity.TOP))
            textIsVisible = !textIsVisible
            binding.text.visibility = if(textIsVisible) View.VISIBLE else View.GONE
        }
    }
}
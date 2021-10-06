package com.example.nasaapp.view.animation

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ActivityAnimationBonusStartBinding

class AnimationActivityBonus : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationBonusStartBinding

    private var show = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnimationBonusStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backgroundImage.setOnClickListener {
            val constraintSet = ConstraintSet()

            val transition = ChangeBounds()
            show = !show
            if (show) {
                constraintSet.clone(this, R.layout.activity_animation_bonus_end)
                transition.interpolator = AnticipateOvershootInterpolator(2.0f)
                transition.duration = 600
            } else {
                constraintSet.clone(this, R.layout.activity_animation_bonus_start)
                transition.interpolator = AnticipateOvershootInterpolator(2.0f)
                transition.duration = 600
            }
            TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
            constraintSet.applyTo(binding.constraintContainer)
        }


    }
}
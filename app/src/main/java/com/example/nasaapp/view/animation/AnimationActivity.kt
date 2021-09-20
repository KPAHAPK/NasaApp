package com.example.nasaapp.view.animation


import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.*
import com.example.nasaapp.databinding.ActivityAnimationEnlargeBinding
import com.example.nasaapp.databinding.ActivityAnimationPathTransactionBinding

class AnimationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationPathTransactionBinding

    private var toRightAnimation = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationPathTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.button.setOnClickListener {
//            val changeBounds = ChangeBounds()
//            changeBounds.setPathMotion(ArcMotion())
//            changeBounds.duration = 500
//            TransitionManager.beginDelayedTransition(
//                binding.transitionsContainer,
//                changeBounds
//            )
//
//            toRightAnimation = !toRightAnimation
//            val params = binding.button.layoutParams as FrameLayout.LayoutParams
//            params.gravity =
//                if (toRightAnimation) Gravity.END or Gravity.BOTTOM else Gravity.START or Gravity.TOP
//            binding.button.layoutParams = params
//        }
        binding.button.apply {
            setOnClickListener{
                toRightAnimation = !toRightAnimation
                val changeBounds = ChangeBounds()
                changeBounds.setPathMotion(ArcMotion())
                changeBounds.duration = 2000
                TransitionManager.beginDelayedTransition(binding.transitionsContainer, changeBounds)

                val params = binding.button.layoutParams as FrameLayout.LayoutParams
                params.gravity = if (toRightAnimation){
                   Gravity.END or Gravity.BOTTOM
                }else{
                    Gravity.TOP or Gravity.START
                }
                binding.button.layoutParams = params
            }
        }
    }

}
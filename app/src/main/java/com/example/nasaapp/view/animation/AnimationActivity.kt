package com.example.nasaapp.view.animation


import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.example.nasaapp.databinding.ActivityAnimationEnlargeBinding

class AnimationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationEnlargeBinding

    private var isExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationEnlargeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.apply {
            setOnClickListener {
//                isExpanded = !isExpanded
//                val set = TransitionSet()
//                    .addTransition(ChangeBounds())
//                    .addTransition(ChangeImageTransform())
//
//                scaleType = if (isExpanded) {
//                    ImageView.ScaleType.CENTER_CROP
//                } else {
//                    ImageView.ScaleType.FIT_CENTER
//                }
//                TransitionManager.beginDelayedTransition(binding.container, set)
//            }

                isExpanded = !isExpanded
                TransitionManager.beginDelayedTransition(
                    binding.container, TransitionSet()
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeImageTransform())
                )

                val params: ViewGroup.LayoutParams = binding.imageView.layoutParams
                params.height =
                    if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
                binding.imageView.layoutParams = params
                binding.imageView.scaleType =
                    if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
            }

        }
    }
}
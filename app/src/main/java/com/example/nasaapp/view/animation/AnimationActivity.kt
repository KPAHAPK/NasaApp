package com.example.nasaapp.view.animation


import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.transition.*
import com.example.nasaapp.databinding.ActivityAnimationShuffleBinding

class AnimationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationShuffleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationShuffleBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val titles: MutableList<String> = ArrayList()
//        for (i in 0..4) {
//            titles.add(String.format("Item %d", i + 1))
//        }
//        createViews(binding.transitionsContainer, titles)
//        binding.button.setOnClickListener {
//            TransitionManager.beginDelayedTransition(binding.transitionsContainer, ChangeBounds())
//            titles.shuffle()
//            createViews(binding.transitionsContainer, titles)
//        }
//    }
//
//    private fun createViews(layout: ViewGroup, titles: List<String>) {
//        layout.removeAllViews()
//        for (title in titles) {
//            val textView = TextView(this)
//            textView.text = title
//            textView.gravity = Gravity.CENTER_HORIZONTAL
//            ViewCompat.setTransitionName(textView, title)
//            layout.addView(textView)
//       }

        val titles:MutableList<String> = ArrayList()
        for (i in 0..4){
            titles.add("Item $i")
        }
        binding.button.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.transitionsContainer, ChangeBounds())
            binding.transitionsContainer.removeAllViews()
            titles.shuffle()
            for (title in titles){
                binding.transitionsContainer.addView(TextView(this).apply {
                    text = title
                    ViewCompat.setTransitionName(this, title)
                    gravity = Gravity.CENTER_HORIZONTAL
                })
            }
        }
    }

}
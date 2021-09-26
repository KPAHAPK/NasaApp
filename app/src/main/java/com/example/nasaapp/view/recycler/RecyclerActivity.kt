package com.example.nasaapp.view.recycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.databinding.ActivityRecyclerBinding


class RecyclerActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecyclerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = mutableListOf<Data>()
        repeat(10) {
            if (it % 2 == 0) {
                data.add(Data("Earth"))
            } else {
                data.add(Data("Mars", ""))
            }
        }
        data.add(0, Data("Header"))

        binding.recyclerView.adapter = RecyclerActivityAdapter(
            object : OnListItemClickListener {
                override fun onItemClick(data: Data) {
                    Toast.makeText(this@RecyclerActivity, data.someText, Toast.LENGTH_SHORT).show()
                }
            }, data
        )
    }
}
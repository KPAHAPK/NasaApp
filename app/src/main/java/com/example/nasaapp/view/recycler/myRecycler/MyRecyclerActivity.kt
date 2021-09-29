package com.example.nasaapp.view.recycler.myRecycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nasaapp.databinding.ActivityRecyclerBinding

class MyRecyclerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerBinding
    private lateinit var adapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteList = mutableListOf<Pair<Note, Boolean>>()

        val layoutManager = LinearLayoutManager(binding.recyclerView.context)
        binding.recyclerView.layoutManager = layoutManager

        adapter = MyRecyclerViewAdapter(noteList, object : OnNoteClickListener {
            override fun onNoteClick(note: Note) {
                Toast.makeText(this@MyRecyclerActivity, note.name, Toast.LENGTH_SHORT).show()
            }

        })
        binding.recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(MyItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.recyclerActivityFAB.setOnClickListener {
            adapter.appendNote()
        }

    }

}
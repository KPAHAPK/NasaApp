package com.example.nasaapp.view.recycler.myRecycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaapp.databinding.ActivityRecyclerBinding

class MyRecyclerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecyclerBinding
    private lateinit var myAdapter: MyRecyclerViewAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noteList = mutableListOf<Pair<Note, Boolean>>()

       val layoutManager = LinearLayoutManager(binding.recyclerView.context)


        myAdapter = MyRecyclerViewAdapter(noteList,
            object : MyOnNoteClickListener {
                override fun onNoteClick(note: Note) {
                    Toast.makeText(this@MyRecyclerActivity, note.name, Toast.LENGTH_SHORT).show()
                }
            }, object : MyOnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }

            })

        binding.recyclerView.apply {
            this.adapter = myAdapter
            this.layoutManager = layoutManager
        }

        itemTouchHelper = ItemTouchHelper(MyItemTouchHelperCallback(myAdapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.recyclerActivityFAB.setOnClickListener {
            myAdapter.appendNote()
        }
    }
}
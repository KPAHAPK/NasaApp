package com.example.nasaapp.view.recycler.recycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.example.nasaapp.databinding.ActivityRecyclerBinding


class RecyclerActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecyclerBinding
    lateinit var itemTouchHelper: ItemTouchHelper
    private var isNewList = false
    private lateinit var adapter: RecyclerActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = mutableListOf<Pair<Data, Boolean>>()
        data.add(Data("Header") to false)
        data.add(Data("Footer") to false)

        adapter = RecyclerActivityAdapter(
            object : OnListItemClickListener {
                override fun onItemClick(data: Data) {
                    Toast.makeText(this@RecyclerActivity, data.someText, Toast.LENGTH_SHORT).show()
                }
            }, object : OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }, data
        )
        val layoutManager = LinearLayoutManager(binding.recyclerView.context)
        val dividerItemDecoration =
            DividerItemDecoration(binding.recyclerView.context, layoutManager.orientation)
//        ContextCompat.getDrawable(applicationContext, R.drawable.bg_mars)?.let {
//            dividerItemDecoration.setDrawable(
//                it
//            )
//        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        binding.recyclerActivityFAB.setOnClickListener {
            adapter.appendItem()
        }

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.recyclerActivityDiffUtilFAB.setOnClickListener { changeAdapterData() }


    }

    private fun changeAdapterData() {
        adapter.setItems(createItemList(isNewList).map { it })
        isNewList = !isNewList
    }

    private fun createItemList(instanceNumber: Boolean): List<Pair<Data, Boolean>> {
        return when (instanceNumber) {
            false -> listOf(
                Pair(Data("Header"), false),
                Pair(Data("Mars", ""), false),
                Pair(Data("Mars", ""), false),
                Pair(Data("Mars", ""), false),
                Pair(Data("Mars", ""), false),
                Pair(Data("Mars", ""), false),
                Pair(Data("Mars", ""), false)
            )
            true -> listOf(
                Pair(Data("Header"), false),
                Pair(Data("Mars", ""), false),
                Pair(Data("Jupiter", ""), false),
                Pair(Data("Mars", ""), false),
                Pair(Data("Neptune", ""), false),
                Pair(Data("Saturn", ""), false),
                Pair(Data("Mars", ""), false)
            )
        }
    }
}
package com.example.nasaapp.view.recycler.myRecycler

interface MyItemTouchHelperAdapter {
    fun onItemDrag(fromPosition: Int, toPosition: Int)
    fun onItemSwipe(position: Int)
}
package com.example.nasaapp.view.recycler.myRecycler

import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MyItemTouchHelperCallback(private val adapter: MyRecyclerViewAdapter): ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
       return makeMovementFlags(UP or DOWN, LEFT or RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onItemDrag(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemSwipe(viewHolder.layoutPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if(actionState != ACTION_STATE_IDLE){
            val vHolder = viewHolder as MyItemTouchHelperViewHolder
            vHolder.onItemSelected()
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        val vHolder = viewHolder as MyItemTouchHelperViewHolder
        vHolder.onItemClear()
    }
}
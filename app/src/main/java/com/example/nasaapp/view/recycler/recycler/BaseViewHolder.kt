package com.example.nasaapp.view.recycler.recycler

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view),
    ItemTouchHelperViewHolder {
    abstract fun bind(pairData: Pair<Data, Boolean>)
    override fun onItemSelected() {
        itemView.setBackgroundColor(Color.RED)
    }

    override fun onItemClear() {
        itemView.setBackgroundColor(0)
    }
}
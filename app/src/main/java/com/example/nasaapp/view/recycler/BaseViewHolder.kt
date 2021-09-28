package com.example.nasaapp.view.recycler

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view), ItemTouchHelperViewHolder {
    abstract fun bind(pairData : Pair<Data, Boolean>)
    override fun onItemSelected() {
        itemView.setBackgroundColor(Color.LTGRAY)
    }

    override fun onItemClear() {
        itemView.setBackgroundColor(0)
    }
}
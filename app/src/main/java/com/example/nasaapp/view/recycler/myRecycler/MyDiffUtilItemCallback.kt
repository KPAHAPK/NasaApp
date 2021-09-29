package com.example.nasaapp.view.recycler.myRecycler

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil

class MyDiffUtilCallback(
    private val oldList: List<Pair<Note, Boolean>>,
    private val newList: List<Pair<Note, Boolean>>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].first.id == newList[newItemPosition].first.id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = oldList[newItemPosition]

        val diff = Bundle()
        with(diff) {
            if (oldItem.first.name != newItem.first.name) {
                putString("name", newItem.first.name)
            }
            if (oldItem.first.description != newItem.first.description) {
                putString("description", newItem.first.description)
            }
            if (oldItem.first.isFavourite != newItem.first.isFavourite) {
                putBoolean("isFavourite", newItem.first.isFavourite)
            }
            if (size() == 0){
                return null
            }
        }
        return diff
    }
}

//class MyDiffUtilItemCallback: DiffUtil.ItemCallback<Note>() {
//    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id
//
//
//    override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem
//}
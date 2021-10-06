package com.example.nasaapp.view.recycler.myRecycler

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
        oldList[oldItemPosition].first.isFavourite == newList[newItemPosition].first.isFavourite

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return NoteChange(
            oldItem,
            newItem
        )
    }
}

data class NoteChange<out T>(
    val oldData: T,
    val newData: T
)


fun <T> myCreateCombinedPayload(payloads: List<NoteChange<T>>): NoteChange<T> {
    assert(payloads.isNotEmpty())
    val firstChange = payloads.first()
    val lastChange = payloads.last()
    return NoteChange(firstChange.oldData, lastChange.newData)
}
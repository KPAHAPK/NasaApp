package com.example.nasaapp.view.recycler.myRecycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ActivityMyRecyclerItemNoteBinding
import kotlinx.android.synthetic.main.activity_my_recycler_item_note.view.*
import kotlinx.android.synthetic.main.activity_recycler_item_mars.view.*
import java.util.*


class MyRecyclerViewAdapter(
    private val noteList: MutableList<Pair<Note, Boolean>>,
    private val myOnNoteClickListener: MyOnNoteClickListener,
    private val onStartDragListener: MyOnStartDragListener
) : RecyclerView.Adapter<MyRecyclerViewAdapter.NoteHolder>(), MyItemTouchHelperAdapter {

    val oldList = noteList.toMutableList()

    companion object {
        var noteCounter = 0
    }

    init {
        repeat(6) {
            appendNote()
        }
    }

    fun appendNote() {
        val position = noteList.size
        noteList.add(position, generateNote() to false)
        notifyItemInserted(position)
    }

    private fun generateNote(): Note {
        ++noteCounter
        return Note("note $noteCounter", "description")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyRecyclerViewAdapter.NoteHolder {
        val binding = ActivityMyRecyclerItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val view = binding.root
        return NoteHolder(view)
    }

    fun setItems(newList: List<Pair<Note, Boolean>>) {
        val result = DiffUtil.calculateDiff(MyDiffUtilCallback(noteList, newList))
        result.dispatchUpdatesTo(this)
        noteList.clear()
        noteList.addAll(newList)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val combinedChange =
                myCreateCombinedPayload(payloads as List<NoteChange<Pair<Note, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.first.name != oldData.first.name) {
                holder.itemView.noteTextView.text =
                    noteList[position].first.name
            }
            if (newData.first.description != oldData.first.description) {
                holder.itemView.noteDescriptionTextView.text =
                    noteList[position].first.description
            }
            if (newData.first.isFavourite != oldData.first.isFavourite) {
                holder.itemView.note_favourite.setBackgroundResource(
                    if (newData.first.isFavourite) {
                        R.drawable.ic_baseline_star_24
                    } else {
                        R.drawable.ic_baseline_star_outline_24_empty
                    }
                )
            }
        }
    }

    override fun onBindViewHolder(holder: MyRecyclerViewAdapter.NoteHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount() = noteList.size

    var lastOpenedNote: Int = -1

    inner class NoteHolder(view: View) : RecyclerView.ViewHolder(view),
        MyItemTouchHelperViewHolder {
        fun bind(pair: Pair<Note, Boolean>) {
            ActivityMyRecyclerItemNoteBinding.bind(itemView).apply {
                noteTextView.apply {
                    text = pair.first.name
                    setOnClickListener {
                        showDescription()
                    }
                }
                noteDescriptionTextView.apply {
                    text = pair.first.description

                    visibility = if (pair.second) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
                noteImageView.apply {
                    setOnClickListener {
                        myOnNoteClickListener.onNoteClick(pair.first)
                    }
                }
                moveItemUp.setOnClickListener {
                    moveUp()
                }
                moveItemDown.setOnClickListener {
                    moveDown()
                }
                addItemImageView.setOnClickListener {
                    addNewNote()
                }
                removeItemImageView.setOnClickListener {
                    removeNote()
                }
                dragHandleImageView.setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        onStartDragListener.onStartDrag(this@NoteHolder)
                    }
                    true
                }
                noteFavourite.apply {
                    setOnClickListener {
                        switchFavourite()
                    }

                }
            }
        }

        private fun switchFavourite() {
            val newList = noteList.toMutableList()
            newList[layoutPosition] = Pair(noteList[layoutPosition].first.copy(), noteList[layoutPosition].second)
            newList[layoutPosition].first.apply {
                isFavourite = !isFavourite
            }
            this@MyRecyclerViewAdapter.setItems(newList)

        }

        private fun showDescription() {
            if (layoutPosition != lastOpenedNote) {
                lastOpenedNote.takeIf { it >= 0 }?.apply {
                    noteList[this] = noteList[this].first to false
                }
                notifyItemChanged(lastOpenedNote)
            }

            noteList[layoutPosition] =
                noteList[layoutPosition].first to !noteList[layoutPosition].second
            lastOpenedNote = layoutPosition
            notifyItemChanged(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 0 }?.also {
                noteList.removeAt(it).apply {
                    noteList.add(it - 1, this)
                }
                notifyItemMoved(layoutPosition, layoutPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < noteList.size }?.also {
                noteList.removeAt(it).apply {
                    noteList.add(it + 1, this)
                }
                notifyItemMoved(layoutPosition, layoutPosition + 1)
            }
        }

        private fun addNewNote() {
            noteList.add(layoutPosition + 1, generateNote() to false)
            notifyItemInserted(layoutPosition + 1)
        }

        private fun removeNote() {
            noteList.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.RED)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    override fun onItemDrag(fromPosition: Int, toPosition: Int) {
        noteList.removeAt(fromPosition).apply {
            noteList.add(toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwipe(position: Int) {
        noteList.removeAt(position)
        notifyItemRemoved(position)
    }
}
package com.example.nasaapp.view.recycler.myRecycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaapp.databinding.ActivityMyRecyclerItemNoteBinding

class MyRecyclerViewAdapter(
    private val noteList: MutableList<Pair<Note, Boolean>>,
    private val onNoteClickListener: OnNoteClickListener
) : RecyclerView.Adapter<MyRecyclerViewAdapter.NoteHolder>(), MyItemTouchHelperAdapter {

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

    lateinit var binding: ActivityMyRecyclerItemNoteBinding

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

    override fun onBindViewHolder(holder: MyRecyclerViewAdapter.NoteHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount() = noteList.size

    var lastOpenedNote: Int = -1

    inner class NoteHolder(view: View) : RecyclerView.ViewHolder(view), MyItemTouchHelperViewHolder {
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
                        onNoteClickListener.onNoteClick(pair.first)
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
            }
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
            noteList.add(layoutPosition+1, generateNote() to false)
            notifyItemInserted(layoutPosition+1)
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
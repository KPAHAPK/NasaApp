package com.example.nasaapp.view.recycler.myRecycler

import java.util.*

class NoteList {

    val noteList: MutableList<Note> = mutableListOf()

    init {
        for (i in 1..10) {
            addNote(Note("note $i", "asdf"))
        }

    }

    fun addNote(note: Note) {
        noteList.add(note)
    }

    fun deleteNote(index: Int) {
        noteList.removeAt(index)
    }

    fun getNote(index: Int) : Note {
        return noteList[index]
    }
}
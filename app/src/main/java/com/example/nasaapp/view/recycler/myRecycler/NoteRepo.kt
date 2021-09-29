package com.example.nasaapp.view.recycler.myRecycler

interface NoteRepo {
    fun addNote()
    fun removeNote(index: Int)
    fun getNote(index: Int): Note
    fun getNoteList(): MutableList<Note>
}
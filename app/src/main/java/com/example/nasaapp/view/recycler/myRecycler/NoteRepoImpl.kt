package com.example.nasaapp.view.recycler.myRecycler

class NoteRepoImpl: NoteRepo{

    private val noteList: MutableList<Note> = mutableListOf()

    companion object {
        var allCreatedNotesCount = 0
    }

    init {
        for (i in 1..6) {
            addNote()
        }

    }

    override fun addNote() {
        ++allCreatedNotesCount
        noteList.add(Note("note $allCreatedNotesCount", "Description"))
    }

    override fun removeNote(index: Int) {
        noteList.removeAt(index)
    }

    override fun getNote(index: Int):Note {
        return noteList[index]
    }

    override fun getNoteList(): MutableList<Note>{
        return noteList
    }
}
package com.example.nasaapp.view.recycler.myRecycler

import java.util.*

data class Note(
    var name: String,
    var description: String,
    var isFavourite: Boolean = false,
    val id: UUID = UUID.randomUUID()
)

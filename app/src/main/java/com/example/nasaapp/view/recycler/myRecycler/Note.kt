package com.example.nasaapp.view.recycler.myRecycler

import java.util.*

data class Note(val name: String, val description: String, val id: UUID = UUID.randomUUID())

package com.example.notepadassignment

import com.google.gson.annotations.SerializedName


data class NoteModel(
    @SerializedName("id") val id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("content") var content: String,
    @SerializedName("selected") var selected: Boolean = false,
)
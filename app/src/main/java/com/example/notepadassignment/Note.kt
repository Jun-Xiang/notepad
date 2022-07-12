package com.example.notepadassignment

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "notes")
data class Note(
    @SerializedName("title") var title: String,
    @SerializedName("content") var content: String,
    @SerializedName("id") @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
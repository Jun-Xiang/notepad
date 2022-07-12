package com.example.notepadassignment

import android.content.Context

object Injection {
    private fun provideDataSource(context: Context) : NoteDao {
        val db = NoteDatabase.getInstance(context)
        return db.noteDao()
    }

    fun provideViewModelFactory(context: Context) : NoteViewModelFactory {
        val dao = provideDataSource(context)
        return NoteViewModelFactory(NoteRepository(dao))
    }
}
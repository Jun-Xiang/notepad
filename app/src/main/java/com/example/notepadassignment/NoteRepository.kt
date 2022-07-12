package com.example.notepadassignment

import androidx.annotation.WorkerThread

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes = noteDao.getAll()

    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    @WorkerThread
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    @WorkerThread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    @WorkerThread
    fun multiDelete(ids: List<Int>) {
        noteDao.multiDelete(ids)
    }

    @WorkerThread
    fun reset() {
        noteDao.reset()
    }
}
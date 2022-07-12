package com.example.notepadassignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class NoteViewModel(private val noteRepo: NoteRepository) : ViewModel() {
    val notes = noteRepo.allNotes.asLiveData()

    fun insert(note: Note) = viewModelScope.launch {
        noteRepo.insert(note)
    }

    fun update(note: Note) = viewModelScope.launch {
        noteRepo.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        noteRepo.delete(note)
    }

    fun multiDelete(ids: List<Int>) = CoroutineScope(Dispatchers.IO).launch{
        noteRepo.multiDelete(ids)
    }

    fun reset() = noteRepo.reset()
}

class NoteViewModelFactory(private val noteRepo: NoteRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(noteRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
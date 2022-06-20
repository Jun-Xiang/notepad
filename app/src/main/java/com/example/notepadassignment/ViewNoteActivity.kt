package com.example.notepadassignment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.notepadassignment.databinding.ActivityViewNoteBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViewNoteActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private lateinit var binding: ActivityViewNoteBinding
    private var isEdit: Boolean = false
    private var noteId = 0
    private val gson = Gson()
    private lateinit var notes: MutableList<NoteModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = application.getSharedPreferences("com.example.notepadassignment", MODE_PRIVATE)
        val noteString = sp.getString("NOTES", "[]")
        notes = gson.fromJson(noteString, object: TypeToken<MutableList<NoteModel>>() {}.type)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        noteId = intent.getIntExtra("NOTE_ID", -1)
        if (noteId >= 0) {
            isEdit = true
            binding.etTitle.setText(notes[noteId].title)
            binding.etNewNote.setText(notes[noteId].content)
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if(isEdit){
                    editNote()
                }else {
                    createNote()
                }
                saveNote(notes)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun createNote() {
        if(binding.etTitle.text.toString().isNotBlank() || binding.etNewNote.text.toString().isNotBlank()){
            val newNote = NoteModel(
                notes.size,
                binding.etTitle.text.toString(),
                binding.etNewNote.text.toString()
            )
            notes.add(newNote)
        }
    }

    fun editNote() {
        val currentNote = notes[noteId]
        currentNote.title = binding.etTitle.text.toString()
        currentNote.content = binding.etNewNote.text.toString()
    }

    fun saveNote(notes: MutableList<NoteModel>) {
        val notesString = gson.toJson(notes)
        val prefEdit = sp.edit()
        prefEdit.putString("NOTES", notesString)
        prefEdit.commit()
    }

}
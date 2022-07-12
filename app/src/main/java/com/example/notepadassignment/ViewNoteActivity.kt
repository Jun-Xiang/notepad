package com.example.notepadassignment

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.notepadassignment.databinding.ActivityViewNoteBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewNoteBinding
    private var isEdit: Boolean = false
    private val gson = Gson()
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (!intent.getStringExtra("NOTE").isNullOrEmpty()) {
            note = gson.fromJson(intent.getStringExtra("NOTE"), object : TypeToken<Note>() {}.type)
            isEdit = true
            binding.etTitle.setText(note.title)
            binding.etNewNote.setText(note.content)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val replyIntent = Intent()
                if (isEdit) {
//                    editNote()
                    note.title = binding.etTitle.text.toString()
                    note.content = binding.etNewNote.text.toString()
                    replyIntent.putExtra("NOTE", gson.toJson(note))
                    setResult(Activity.RESULT_OK, replyIntent)
                } else {
                    if (binding.etTitle.text.toString()
                            .isNotBlank() || binding.etNewNote.text.toString().isNotBlank()
                    ) {
                        replyIntent.putExtra(
                            "NOTE",
                            gson.toJson(
                                Note(
                                    binding.etTitle.text.toString(),
                                    binding.etNewNote.text.toString()
                                )
                            )
                        )
                        setResult(Activity.RESULT_OK, replyIntent)
                    } else {
                        setResult(Activity.RESULT_CANCELED, replyIntent)
                    }
                }
//                saveNote(notes)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    fun createNote() {
//        val newNote = NoteModel(
//            notes.size,
//            binding.etTitle.text.toString(),
//            binding.etNewNote.text.toString()
//        )
//        notes.add(newNote)
//    }
//
//    fun editNote() {
//        val currentNote = notes[noteId]
//        currentNote.title = binding.etTitle.text.toString()
//        currentNote.content = binding.etNewNote.text.toString()
//    }
//
//    fun saveNote(notes: MutableList<NoteModel>) {
//        val notesString = gson.toJson(notes)
//        val prefEdit = sp.edit()
//        prefEdit.putString("NOTES", notesString)
//        prefEdit.commit()
//    }

}
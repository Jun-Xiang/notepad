package com.example.notepadassignment

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notepadassignment.databinding.NotesBinding

class NotesAdapter(
    private var data: List<Note>,
    private val handleClick: (note: Note, toggleSelected: () -> Unit) -> Unit,
    private val handleLongClick: () -> Unit
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    private val _selected: MutableList<Int> = mutableListOf()
    val selected get() = _selected.toList()
    class ViewHolder(val binding: NotesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotesBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = data[position]
        val binding = holder.binding

        binding.title.text = note.title
        binding.content.text = note.content
        binding.clNote.clipToOutline = true
        binding.clNote.setOnClickListener {
            handleClick(note) { toggleSelected(note.id)}
        }
        binding.clNote.setOnLongClickListener {
            handleLongClick()
            handleClick(note) { toggleSelected(note.id)}
            true
        }
        Log.i("TEST", data.toString())
        if (_selected.contains(note.id)) {
            binding.clNote.background = holder.itemView.resources.getDrawable(R.drawable.selected_rounded_note, holder.itemView.context.theme)
        } else {
            binding.clNote.background = holder.itemView.resources.getDrawable(R.drawable.rounded_note, holder.itemView.context.theme)
        }

    }

    override fun getItemCount(): Int = data.size
    fun updateList(newData: List<Note>) {
        Log.i("TEST", newData.toString())
        data = newData;
        notifyDataSetChanged()
    }

    fun resetSelected()  {
        notifyDataSetChanged()
        _selected.clear()
    }
    private fun toggleSelected(id: Int) {
        if(_selected.contains(id)) {
            _selected.remove(id)
        }else {
            _selected.add(id)
        }
        notifyDataSetChanged()
    }

}
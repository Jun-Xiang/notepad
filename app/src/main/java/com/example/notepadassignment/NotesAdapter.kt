package com.example.notepadassignment

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notepadassignment.databinding.NotesBinding

class NotesAdapter(
    private var data: List<NoteModel>,
    private val handleClick: (note: NoteModel, notifyItemChanged: () -> Unit) -> Unit,
    private val handleLongClick: () -> Unit
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
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
            handleClick(note) { notifyItemChanged(position) }
        }
        binding.clNote.setOnLongClickListener {
            handleLongClick()
            handleClick(note) { notifyItemChanged(position) }
            true
        }
        Log.i("TEST", data.toString())
        if (note.selected) {
            binding.clNote.background = holder.itemView.resources.getDrawable(R.drawable.selected_rounded_note, holder.itemView.context.theme)
        } else {
            binding.clNote.background = holder.itemView.resources.getDrawable(R.drawable.rounded_note, holder.itemView.context.theme)

        }

    }

    override fun getItemCount(): Int = data.size
    fun updateList(newData: List<NoteModel>) {
        Log.i("TEST", newData.toString())
        data = newData;
        notifyDataSetChanged()
    }
}
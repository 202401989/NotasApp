package com.efer.notasapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.efer.notasapp.data.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val notes = mutableListOf<Note>()

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtNoteItemTitle)
        val txtContent: TextView = itemView.findViewById(R.id.txtNoteItemContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.txtTitle.text = note.titulo
        holder.txtContent.text = note.contenido
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
}


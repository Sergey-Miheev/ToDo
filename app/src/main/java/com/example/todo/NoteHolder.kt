package com.example.todo

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById(R.id.noteTitle)
    var description: TextView = itemView.findViewById(R.id.noteDescription)
    var date: TextView = itemView.findViewById(R.id.noteDate)
}
package com.example.todo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class NoteAdapter(inputNotesList: ArrayList<Note>)
    : RecyclerView.Adapter<NoteViewHolder>(),Filterable   {

    private var notesList: ArrayList<Note> = inputNotesList
    private var notesFilteredList: ArrayList<Note> = inputNotesList
    var onItemClick: ((Note) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))
    }

    override fun getItemCount(): Int {
        return notesFilteredList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = notesFilteredList[position]

        holder.title.text = note.title
        holder.description.text = note.description
        holder.date.text = note.date

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(note)
        }
    }

    // При вводе текста формирует отфильтрованный список и возвращает его
    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString: String = constraint.toString()
                if (searchString.isNotEmpty()) {
                    val resultList = ArrayList<Note>()
                    for (note in notesList) {
                        if (note.description.lowercase(Locale.ROOT)
                                .contains(searchString.lowercase(Locale.ROOT))
                            || note.title.lowercase(Locale.ROOT)
                                .contains(searchString.lowercase(Locale.ROOT))) {
                            resultList.add(note)
                        }
                    }
                    notesFilteredList = resultList
                } else {
                    notesFilteredList = notesList
                }
                val filterResult = FilterResults()
                filterResult.values = notesFilteredList
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notesFilteredList = results?.values as ArrayList<Note>
                notifyDataSetChanged()
            }
        }
    }
}
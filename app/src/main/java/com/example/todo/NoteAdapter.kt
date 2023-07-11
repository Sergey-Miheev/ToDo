package com.example.todo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import models.DateConverter
import models.NoteModel

class NoteAdapter(context: MainActivity, inputNotesList: ArrayList<NoteModel>)
    : RecyclerView.Adapter<NoteViewHolder>(), Filterable   {

    private var notesList: ArrayList<NoteModel> = inputNotesList
    private var notesFilteredList: ArrayList<NoteModel> = inputNotesList
    private val dbDao: NoteDao = MainDb.getDb(context).getNoteDao()
    var onItemClick: ((NoteModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return notesFilteredList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: NoteModel = notesFilteredList[position]

        holder.title.text = note.title
        holder.description.text = note.description

        val converter = DateConverter()
        holder.date.text = converter.toString(note.date)

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
                    notesFilteredList = dbDao.getFilteredNotes(searchString) as ArrayList<NoteModel>
                    /*for (note in notesList) {
                        if (note.description.lowercase(Locale.ROOT)
                                .contains(searchString.lowercase(Locale.ROOT))
                            || note.title.lowercase(Locale.ROOT)
                                .contains(searchString.lowercase(Locale.ROOT))) {
                            resultList.add(note)
                        }
                    }*/

                } else {
                    notesFilteredList = ArrayList(notesList)
                }
                val filterResult = FilterResults()
                filterResult.values = notesFilteredList
                return filterResult
            }

            @Suppress("UNCHECKED_CAST")
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notesFilteredList = results?.values as ArrayList<NoteModel>
                notifyDataSetChanged()
            }
        }
    }
}
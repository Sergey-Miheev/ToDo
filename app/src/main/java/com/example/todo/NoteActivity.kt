package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.todo.databinding.ActivityNoteBinding
import models.NoteModel
import java.util.Date

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    private var note: NoteModel = NoteModel(null, "", "", Date(), false)
    private lateinit var dbDao: NoteDao
    private fun initViews() {
        val arguments = intent.extras

        if (arguments != null) {
            val noteId: Int = arguments.getInt("noteId")

            val titleView: EditText = findViewById(R.id.edit_note_title)
            val descriptionView: EditText = findViewById(R.id.edit_note_description)
            descriptionView.setText("")
            titleView.setText("")

            // зачем как live Data
            if (noteId > 0) {
                dbDao.getNoteById(noteId).asLiveData().observe(this) {
                    note = it

                    descriptionView.setText(note.description)
                    titleView.setText(note.title)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbDao = MainDb.getDb(this).getNoteDao()

        initViews()

        binding.saveNoteFab.setOnClickListener {
            if (note.id != null) {
                Thread{
                    dbDao.updateNote(note)
                }.start()
            } else {
                note.title = binding.editNoteTitle.text.toString()
                note.description = binding.editNoteDescription.text.toString()
                note.date = Date()
                Thread{
                    dbDao.insertNote(note)
                }.start()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
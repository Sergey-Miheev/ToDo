package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.todo.databinding.ActivityNoteEditBinding
import models.NoteModel
import java.util.Date

class NoteEdit : AppCompatActivity() {

    private lateinit var binding: ActivityNoteEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = MainDb.getDb(this)

        binding.saveNoteFab.setOnClickListener {
            val note = NoteModel(null,
                binding.editNoteTitle.text.toString(),
                binding.editNoteDescription.text.toString(),
                Date(),
                false
            )
            Thread{
                db.getDao().insertNote(note)
            }.start()
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        extracted()
    }

    private fun extracted() {
        //val note = intent.getParcelableExtra()<Note>("note", Note::class.java)
        val arguments = intent.extras

        var note: Note = Note("", "", "")
        if (arguments != null) {

            @Suppress("DEPRECATION")
            note = arguments.getSerializable(Note::class.java.simpleName) as Note
        }
        val description: EditText = findViewById(R.id.edit_note_description)
        description.setText(note.description)
        val title: EditText = findViewById(R.id.edit_note_title)
        title.setText(note.title)
    }
}
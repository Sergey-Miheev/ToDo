package com.example.todo

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NoteEdit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)

        //val note = intent.getParcelableExtra()<Note>("note", Note::class.java)
        val arguments = intent.extras

        var note: Note = Note("","")
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
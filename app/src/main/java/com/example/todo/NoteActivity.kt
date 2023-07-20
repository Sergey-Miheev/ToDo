package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.todo.databinding.ActivityNoteBinding
import models.NoteModel
import java.util.Date

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    private var note: NoteModel = NoteModel(null, "", "", Date(), false)
    private lateinit var noteDao: NoteDao
    private fun initViews() {
        val arguments = intent.extras

        val pinIcon = binding.noteEditToolbarPinIcon
        val pinIconSmall = binding.noteEditToolbarPinIconSmall

        if (arguments != null) {
            val noteId: Int = arguments.getInt("noteId")

            val titleView: EditText = binding.editNoteTitle
            val descriptionView: EditText = binding.editNoteDescription
            descriptionView.setText("")
            titleView.setText("")

            if (noteId > 0) {
                noteDao.getNoteById(noteId).asLiveData().observe(this) {
                    note = it

                    descriptionView.setText(note.description)
                    titleView.setText(note.title)
                    // Если заметка закреплена, то внутреннюю(меньшую) иконку цвета фона экрана делаем невидимой
                    if (note.pinned) {
                        pinIconSmall.visibility = View.INVISIBLE
                    }
                }
            }
        }

        pinIcon.setOnClickListener {
            if (note.pinned) {
                pinIconSmall.visibility = View.VISIBLE
                note.pinned = false
            } else {
                pinIconSmall.visibility = View.INVISIBLE
                note.pinned = true
            }
        }
    }

    private fun initMenu() {
        val toolbarMenu = binding.noteMenu
        toolbarMenu.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteDao = MainDb.getDb(this).getNoteDao()

        initViews()

        initMenu()

        binding.saveNoteFab.setOnClickListener {
            if (note.id != null) {
                Thread {
                    noteDao.updateNote(note)
                }.start()
            } else {
                note.title = binding.editNoteTitle.text.toString()
                note.description = binding.editNoteDescription.text.toString()
                note.date = Date()
                Thread {
                    noteDao.insertNote(note)
                }.start()
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("isNotes", true)
            startActivity(intent)
        }
    }
}
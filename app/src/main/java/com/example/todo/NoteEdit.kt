package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.example.todo.databinding.ActivityNoteEditBinding
import models.NoteModel
import java.util.Date

class NoteEdit : AppCompatActivity() {

    private lateinit var binding: ActivityNoteEditBinding
    private var note: NoteModel = NoteModel(null, "", "", Date(), false)
    private lateinit var db: MainDb
    private fun initViews() {
        val arguments = intent.extras

        if (arguments != null) {
            val noteId: Int = arguments.getInt("noteId")

            val titleView: EditText = findViewById(R.id.edit_note_title)
            val descriptionView: EditText = findViewById(R.id.edit_note_description)
            descriptionView.setText("")
            titleView.setText("")

            if (noteId > 0) {
                db.getNoteDao().getNoteById(noteId).asLiveData().observe(this) {
                    note = it

                    descriptionView.setText(note.description)
                    titleView.setText(note.title)
                }
            }
            //note = arguments.getSerializable(Note::class.java.simpleName) as Note
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MainDb.getDb(this)

        initViews()

        binding.saveNoteFab.setOnClickListener {
            val note = NoteModel(null,
                binding.editNoteTitle.text.toString(),
                binding.editNoteDescription.text.toString(),
                Date(),
                false
            )
            Thread{
                db.getNoteDao().insertNote(note)
            }.start()
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}

/*
<androidx.appcompat.widget.Toolbar
        android:id="@+id/schedule_toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        android:minHeight="?attr/actionBarSize"
        android:theme="@style/Theme.ToDo"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:menu="@menu/main_menu"
        app:title="on.time"
        app:titleTextColor="@color/white">

        <ImageView
            android:id="@+id/schedule_toolbar_remindersIcon"
            android:layout_width="wrap_content"
            android:layout_height="40dp"
            android:layout_gravity="end"
            android:layout_marginEnd="10dp"
            android:contentDescription="@string/remindersIcon"
            android:src="@drawable/bell"
            app:layout_constraintRight_toRightOf="parent"
            />
    </androidx.appcompat.widget.Toolbar>
 */
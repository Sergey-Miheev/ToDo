package com.example.todo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ActivityMainBinding
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import models.NoteModel

class MainActivity : AppCompatActivity() {
    private var notesRecyclerView: RecyclerView? = null
    private lateinit var notesList: List<NoteModel>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteSearchView: SearchView
    private lateinit var binding: ActivityMainBinding

    private fun setsFAB() {
        // добавляем кнопку перехода на экран создания напоминания
        val fabView = findViewById<SpeedDialView>(R.id.noteExpandedFAB)
        fabView.addActionItem(
            SpeedDialActionItem.Builder(R.id.note_fab_schedule_icon, R.drawable.calendar_icon)
                .create()
        )

        // добавляем кнопку перехода на экран создания заметки
        fabView.addActionItem(
            SpeedDialActionItem.Builder(R.id.note_fab_note_icon, R.drawable.note_icon)
                .create()
        )

        // обработчик клика на кнопку создания напоминания
        fabView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.note_fab_schedule_icon -> {
                    fabView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })

        // обработчик клика на кнопку создания заметки
        fabView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.note_fab_note_icon -> {
                    val intent: Intent = Intent(this, NoteEdit::class.java)
                    intent.putExtra("noteId", 0)
                    startActivity(intent)
                    fabView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })
    }

    private fun getNotesList() {

        val db = MainDb.getDb(this)
        db.getNoteDao().getAllNotes().asLiveData().observe(this) { dbNotesList ->
            notesList = dbNotesList

            noteAdapter = NoteAdapter(notesList)
            notesRecyclerView?.adapter = noteAdapter

            // при нажатии на заметку, переходим на экран редактирования заметки
            noteAdapter.onItemClick = {noteItem ->
                val intent: Intent = Intent(this, NoteEdit::class.java)
                intent.putExtra("noteId", noteItem.id)
                startActivity(intent)
            }
        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS )
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // получение вьюшки фильтра заметок - SearchView
        noteSearchView = findViewById(R.id.searchNoteView)
        // меняем цвет иконки удаления введённого текста
        val cancelIcon = noteSearchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.rgb(79,79,79))

        // убираем фокусировку курсора на строке поиска(при открытии экрана)
        noteSearchView.clearFocus()
        noteSearchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
        android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(typedText: String?): Boolean {
                noteAdapter.filter.filter(typedText)
                return false
            }
        })

        /*val switchButton: SwitchCompat = findViewById(R.id.switchButton)

        switchButton.setOnCheckedChangeListener { _, isChecked: Boolean ->
            if (isChecked) {
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent: Intent = Intent(this, ScheduleActivity::class.java)
                startActivity(intent)
            }
        }*/

        // иницилизация списка заметок - RecyclerView
        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        notesRecyclerView?.setHasFixedSize(true)
        notesRecyclerView?.layoutManager = LinearLayoutManager(this)

        getNotesList()

        setsFAB()
    }

    @SuppressLint("ResourceType")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
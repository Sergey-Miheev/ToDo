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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView

class MainActivity : AppCompatActivity() {
    private var notesRecyclerView: RecyclerView? = null
    private lateinit var notesList: ArrayList<Note>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteSearchView: SearchView

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS )
        supportActionBar?.hide()

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

        // иницилизация списка заметок - RecyclerView
        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        notesRecyclerView?.setHasFixedSize(true)
        notesRecyclerView?.layoutManager = LinearLayoutManager(this)

        notesList = ArrayList()
        notesList.add(Note("DATSUN ON-DO the best car in the Russia and all SNG area", "Start"))
        notesList.add(Note("nope", "End"))
        notesList.add(Note("neutral", "Middle"))
        notesList.add(Note("yeah", "Start"))
        notesList.add(Note("nope", "End"))
        notesList.add(Note("neutral", "Middle"))
        notesList.add(Note("yeah", "Start"))
        notesList.add(Note("nope", "End"))
        notesList.add(Note("neutral", "Middle"))

        noteAdapter = NoteAdapter(notesList)
        notesRecyclerView?.adapter = noteAdapter

        // при нажатии на заметку, переходим на экран редактирования заметки
        noteAdapter.onItemClick = {
            val intent: Intent = Intent(this, NoteEdit::class.java)
            intent.putExtra(Note::class.java.simpleName , it)
            startActivity(intent)
        }

        // добавляем кнопку перехода на экран создания напоминания
        val fabView = findViewById<SpeedDialView>(R.id.speedDial)
        fabView.addActionItem(
            SpeedDialActionItem.Builder(R.id.fab_schedule_icon, R.drawable.calendar_icon)
                .create()
        )

        // добавляем кнопку перехода на экран создания заметки
        fabView.addActionItem(
            SpeedDialActionItem.Builder(R.id.fab_note_icon, R.drawable.note_icon)
                .create()
        )

        // обработчик клика на кнопку создания напоминания
        fabView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_schedule_icon -> {
                    fabView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })

        // обработчик клика на кнопку создания заметки
        fabView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.fab_note_icon -> {
                    val intent: Intent = Intent(this, NoteEdit::class.java)
                    startActivity(intent)
                    fabView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })
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
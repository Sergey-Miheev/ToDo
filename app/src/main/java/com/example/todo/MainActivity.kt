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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import models.NoteModel
import java.util.Timer
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity() {
    private var notesRecyclerView: RecyclerView? = null
    private lateinit var notesList: ArrayList<NoteModel>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteSearchView: SearchView
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbDao: NoteDao

    private fun getNotesList() {

        dbDao.getAllNotes().asLiveData().observe(this) { dbNotesList ->
            notesList = ArrayList(dbNotesList)

            noteAdapter = NoteAdapter(this, notesList)
            notesRecyclerView?.adapter = noteAdapter

            // при нажатии на заметку, переходим на экран редактирования заметки
            noteAdapter.onItemClick = { noteItem ->
                val intent = Intent(this, NoteActivity::class.java)
                intent.putExtra("noteId", noteItem.id)
                startActivity(intent)
            }
        }
    }

    private fun setDeleteNoteBySwipe() {
        var noteIsDeleted: Boolean

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val durationUndoPossibility = 3000

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val deletedNote: NoteModel =
                    notesList[position]

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                notesList.removeAt(position)

                // below line is to notify our item is removed from adapter.
                noteAdapter.notifyItemRemoved(position)

                noteIsDeleted = true

                // below line is to display our snack-bar with action.
                Snackbar.make(
                    notesRecyclerView!!,
                    "Deleted " + deletedNote.title,
                    Snackbar.LENGTH_LONG
                )
                    .setDuration(durationUndoPossibility)
                    .setAction(
                        "Undo"
                    ) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        notesList.add(position, deletedNote)

                        // below line is to notify item is
                        // added to our adapter class.
                        noteAdapter.notifyItemInserted(position)

                        noteIsDeleted = false
                    }.show()

                Timer().schedule(timerTask {
                    if (noteIsDeleted) {
                        Thread {
                            dbDao.deleteNote(deletedNote)
                        }.start()
                    }
                }, durationUndoPossibility.toLong())
            }

            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(notesRecyclerView)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // получение вьюшки фильтра заметок - SearchView
        noteSearchView = findViewById(R.id.searchNoteView)
        // меняем цвет иконки удаления введённого текста
        val cancelIcon =
            noteSearchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.rgb(79, 79, 79))

        // убираем фокусировку курсора на строке поиска(при открытии экрана)
        noteSearchView.clearFocus()
        noteSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
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

        dbDao = MainDb.getDb(this).getNoteDao()

        getNotesList()

        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        setDeleteNoteBySwipe()
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
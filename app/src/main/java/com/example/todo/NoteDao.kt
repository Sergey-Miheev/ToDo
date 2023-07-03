package com.example.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import models.NoteModel

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note: NoteModel)
    @Delete
    fun deleteNote(note: NoteModel)
    @Query("SELECT * FROM note ")
    fun getAllNotes(): Flow<List<NoteModel>>
    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteById(noteId: Int): Flow<NoteModel>
    @Query("SELECT * FROM note WHERE title LIKE '%' || :search  || '%' OR description LIKE '%' || :search  || '%'")
    fun getFilteredNotes(search: String): List<NoteModel>
}
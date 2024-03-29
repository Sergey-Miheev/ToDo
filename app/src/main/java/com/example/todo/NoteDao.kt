package com.example.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import models.NoteModel

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note: NoteModel)
    @Delete
    fun deleteNote(note: NoteModel)
    @Update
    fun updateNote(note: NoteModel)
    @Query("SELECT * FROM note ORDER BY pinned DESC, date DESC")
    fun getAllNotes(): Flow<List<NoteModel>>
    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNoteById(noteId: Int): Flow<NoteModel>
    @Query("SELECT * FROM note WHERE title LIKE '%' || :search  || '%' OR description LIKE '%' || :search  || '%' ORDER BY pinned DESC, date DESC")
    fun getFilteredNotes(search: String): List<NoteModel>
}
package com.example.todo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import models.NoteModel

@Dao
interface Dao {
    @Insert
    fun insertNote(note: NoteModel)
    @Query("SELECT * FROM note ")
    fun getAllNotes(): Flow<List<NoteModel>>
}
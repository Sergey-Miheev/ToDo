package com.example.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import models.DateConverter
import models.NoteModel

@Database (entities = [NoteModel::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class MainDb: RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object{
        fun getDb(context: Context): MainDb{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "ToDo.db"
            ).build()
        }
    }

}
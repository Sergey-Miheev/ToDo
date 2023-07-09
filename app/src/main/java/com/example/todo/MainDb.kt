package com.example.todo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import models.NoteModel
import models.ScheduleModel

@Database (entities = [NoteModel::class, ScheduleModel::class], version = 1)
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
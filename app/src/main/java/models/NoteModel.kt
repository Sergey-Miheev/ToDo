package models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.Date

@Entity (tableName="note")
@TypeConverters(DateConverter::class)
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "date")
    var date: Date,
    @ColumnInfo(name = "pinned")
    var pinned: Boolean,
    )

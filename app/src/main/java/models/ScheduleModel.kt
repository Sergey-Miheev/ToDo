package models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleModel (
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "isClosed")
    var isClosed: Boolean,
    @ColumnInfo("isFullday")
    var isFullday: Boolean,
    @ColumnInfo("startDateTime")
    var startDateTime: String,
    @ColumnInfo("finishDateTime")
    var finishDateTime: String,
    @ColumnInfo(name = "repeat")
    var repeat: Byte,
    @ColumnInfo(name = "reminder")
    var reminder: Byte,
    @ColumnInfo(name = "place")
    var place: String,
    @ColumnInfo(name = "notes")
    var notes: String,
)
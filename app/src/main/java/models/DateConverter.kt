package models

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateConverter {
    @TypeConverter
    fun toString(date: Date): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }

    @SuppressLint("SimpleDateFormat")
    @TypeConverter
    fun fromString(date: String): Date {
        val format = SimpleDateFormat("dd/MM/yyyy") // Формат даты в строке
        return format.parse(date)!!
    }
}
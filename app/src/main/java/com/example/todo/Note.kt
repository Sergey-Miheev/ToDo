package com.example.todo

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Класс заметка, содержит заголовок, описание и дату создания
class Note(description: String, title: String): Serializable {

    private fun getCurrentDate(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    var title: String = title
        set(value) {
            field = value
        }
        get() {
            return field
        }
    var date: String = getCurrentDate()
        get() {
            return field
        }
    var description: String = description
        set(value) {
            field = value
        }
        get() {
            return field
        }


}
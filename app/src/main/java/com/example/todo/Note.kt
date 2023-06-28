package com.example.todo

import java.io.Serializable

// Класс заметка, содержит заголовок, описание и дату создания
//@Parcelize
class Note(title: String, description: String, date: String): Serializable {

    /*private fun getCurrentDate(date: Date): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }*/

    var title: String = title
        set(value) {
            field = value
        }
        get() {
            return field
        }
    var date: String = date
        set(value) {
            field = value
        }
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
package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView

class ScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        // добавляем кнопку перехода на экран создания напоминания
        val fabView = findViewById<SpeedDialView>(R.id.scheduleExpandedFAB)
        fabView.addActionItem(
            SpeedDialActionItem.Builder(R.id.schedule_fab_schedule_icon, R.drawable.calendar_icon)
                .create()
        )

        // добавляем кнопку перехода на экран создания заметки
        fabView.addActionItem(
            SpeedDialActionItem.Builder(R.id.schedule_fab_note_icon, R.drawable.note_icon)
                .create()
        )

        // обработчик клика на кнопку создания напоминания
        fabView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.schedule_fab_schedule_icon -> {
                    fabView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })

        // обработчик клика на кнопку создания заметки
        fabView.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { actionItem ->
            when (actionItem.id) {
                R.id.schedule_fab_note_icon -> {
                    val intent: Intent = Intent(this, NoteActivity::class.java)
                    startActivity(intent)
                    fabView.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })
    }

}
package com.example.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.OnClickListener
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {

    private var dateTimePick: OnClickListener = OnClickListener {dateTimePickerView ->
        val view: EditText = dateTimePickerView as EditText
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedDateTime = Calendar.getInstance()
                selectedDateTime.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute)

                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                val formattedDateTime = dateFormat.format(selectedDateTime.time)

                view.setText(formattedDateTime)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }, year, month, day)

        datePickerDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val dateTimeOfStartPicker = findViewById<EditText>(R.id.startFrom_picker)
        dateTimeOfStartPicker.setOnClickListener(dateTimePick)

        val dateTimeOfFinishPicker = findViewById<EditText>(R.id.finishFrom_picker)
        dateTimeOfFinishPicker.setOnClickListener(dateTimePick)
    }
}
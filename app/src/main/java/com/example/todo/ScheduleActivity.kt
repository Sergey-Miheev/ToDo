package com.example.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {

    private val context = this

    private var dateTimePick: OnClickListener = OnClickListener { dateTimePickerView ->
        val view: EditText = dateTimePickerView as EditText
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                    val selectedDateTime = Calendar.getInstance()
                    selectedDateTime.set(
                        selectedYear,
                        selectedMonth,
                        selectedDay,
                        selectedHour,
                        selectedMinute
                    )

                    val dateFormat =
                        SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault())
                    val formattedDateTime = dateFormat.format(selectedDateTime.time)

                    view.setText(formattedDateTime)
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                timePickerDialog.show()
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun setupSpinner(spinner: Spinner, items: Array<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedView = view as TextView
                selectedView.setTextColor(ContextCompat.getColor(context, R.color.light_gray))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обработчик пустого выбора
            }
        }
    }

    private fun setupCloseTickView() {
        val closeTickView = findViewById<ImageView>(R.id.upbar_finishTick)
        closeTickView.setOnClickListener {
            val intent = Intent(this, ScheduleListActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        // получаем текущую дату и время
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault())
        val formattedDateTime = dateFormat.format(currentDate.time)

        // устанавливаем текущую дату и время и добавляем возможность выбрать дату и время
        val dateTimeOfStartPicker = findViewById<EditText>(R.id.startFrom_picker)
        dateTimeOfStartPicker.setText(formattedDateTime)
        dateTimeOfStartPicker.setOnClickListener(dateTimePick)

        // устанавливаем текущую дату и время и добавляем возможность выбрать дату и время
        val dateTimeOfFinishPicker = findViewById<EditText>(R.id.finishFrom_picker)
        dateTimeOfFinishPicker.setText(formattedDateTime)
        dateTimeOfFinishPicker.setOnClickListener(dateTimePick)

        var spinnerItems = resources.getStringArray(R.array.repeat_items)
        val repeatSpinner = findViewById<Spinner>(R.id.repeat_spinner)
        setupSpinner(repeatSpinner, spinnerItems)

        val reminderSpinner = findViewById<Spinner>(R.id.reminder_spinner)
        spinnerItems = resources.getStringArray(R.array.reminder_items)
        setupSpinner(reminderSpinner, spinnerItems)

        setupCloseTickView()
    }
}
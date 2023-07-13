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
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.example.todo.databinding.ActivityScheduleBinding
import models.ScheduleModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScheduleActivity : AppCompatActivity() {

    private val context = this
    private lateinit var binding: ActivityScheduleBinding
    private lateinit var scheduleDao: ScheduleDao
    private fun getCurrentDateTime(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault())
        return dateFormat.format(currentDate.time)
    }

    private var schedule: ScheduleModel =
        ScheduleModel(
            null, "", isClosed = false, isFullday = false, startDateTime = getCurrentDateTime(),
            finishDateTime = getCurrentDateTime(), repeat = 0, reminder = 0, place = "", notes = ""
        )

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
        // android.R.layout.simple_spinner_item, для задания размеров
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedView = view as TextView
                selectedView.setTextColor(ContextCompat.getColor(context, R.color.light_gray))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Обработчик пустого выбора
            }
        }
    }

    private fun setupCloseTickView() {
        val closeTickView = binding.upbarFinishTick
        closeTickView.setOnClickListener {
            schedule.title = binding.titleName.text.toString()
            schedule.isClosed = binding.upbarCloseCheckBox.isChecked
            schedule.isFullday = binding.fulldaySwitch.isChecked
            schedule.startDateTime = binding.startFromPicker.text.toString()
            schedule.finishDateTime = binding.finishFromPicker.text.toString()
            schedule.repeat = binding.repeatSpinner.selectedItemPosition.toByte()
            schedule.reminder = binding.reminderSpinner.selectedItemPosition.toByte()
            schedule.place = binding.placeEdit.text.toString()
            schedule.notes = binding.descriptionEdit.text.toString()
            if (schedule.id == null) {
                Thread {
                    scheduleDao.insertSchedule(schedule)
                }.start()
            } else {
                Thread {
                    scheduleDao.updateSchedule(schedule)
                }.start()
            }

            val intent = Intent(this, ScheduleListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scheduleDao = MainDb.getDb(this).getScheduleDao()

        val repeatSpinner = binding.repeatSpinner
        var spinnerItems = resources.getStringArray(R.array.repeat_items)
        setupSpinner(repeatSpinner, spinnerItems)

        val reminderSpinner = binding.reminderSpinner
        spinnerItems = resources.getStringArray(R.array.reminder_items)
        setupSpinner(reminderSpinner, spinnerItems)

        val arguments = intent.extras
        if (arguments != null) {
            val idSchedule = arguments.getInt("idSchedule")
            scheduleDao.getScheduleById(idSchedule).asLiveData().observe(this) { scheduleFromDb ->
                schedule = scheduleFromDb

                binding.upbarCloseCheckBox.isChecked = schedule.isClosed
                binding.titleName.setText(schedule.title)
                binding.fulldaySwitch.isChecked = schedule.isFullday
                binding.startFromPicker.setText(schedule.startDateTime)
                binding.finishFromPicker.setText(schedule.finishDateTime)
                binding.repeatSpinner.setSelection(schedule.repeat.toInt())
                binding.reminderSpinner.setSelection(schedule.reminder.toInt())
                binding.placeEdit.setText(schedule.place)
                binding.descriptionEdit.setText(schedule.notes)
            }
        } else {
            // устанавливаем текущую дату и время
            val formattedDateTime = getCurrentDateTime()
            binding.startFromPicker.setText(formattedDateTime)

            binding.finishFromPicker.setText(formattedDateTime)

        }
        //добавляем возможность выбрать дату и время
        binding.startFromPicker.setOnClickListener(dateTimePick)

        binding.finishFromPicker.setOnClickListener(dateTimePick)

        setupCloseTickView()
    }
}
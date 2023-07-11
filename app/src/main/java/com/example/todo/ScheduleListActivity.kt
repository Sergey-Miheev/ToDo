package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.ActivityScheduleListBinding
import models.ScheduleModel

class ScheduleListActivity : AppCompatActivity() {

    private lateinit var schedulesList: List<ScheduleModel>
    private lateinit var schedulesListAdapter: ScheduleAdapter
    private lateinit var scheduleDao: ScheduleDao
    private lateinit var binding: ActivityScheduleListBinding
    private fun getScheduleListFromDb(selectedMonth: String, selectedYear: String) {
        scheduleDao.getMonthSchedules("$selectedMonth $selectedYear").asLiveData()
            .observe(this@ScheduleListActivity) { dbSchedulesList ->
                schedulesList = dbSchedulesList
                schedulesListAdapter = ScheduleAdapter(schedulesList)

                binding.scheduleListView.adapter = schedulesListAdapter
            }
    }
    private fun setupSchedulesRecyclerView() {
        getScheduleListFromDb(binding.calendarView.getSelectedMonth(), binding.calendarView.getSelectedYear())
        binding.calendarView.setMonthChangeListener(object : CalendarView.DataChangeListener {
            override fun onDataChanged(selectedMonth: String, selectedYear: String) {
                getScheduleListFromDb(selectedMonth, selectedYear)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        scheduleDao = MainDb.getDb(this).getScheduleDao()
        binding.scheduleListView.setHasFixedSize(true)
        binding.scheduleListView.layoutManager = LinearLayoutManager(this)

        setupSchedulesRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.calendarView.removeMonthChangeListener()
    }
}
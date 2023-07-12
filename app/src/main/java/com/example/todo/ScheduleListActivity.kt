package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
                if (dbSchedulesList.isEmpty()) {
                    binding.schuduleListMissingMsg.visibility = View.VISIBLE
                } else {
                    binding.schuduleListMissingMsg.visibility = View.GONE
                }
                schedulesList = dbSchedulesList
                schedulesListAdapter = ScheduleAdapter(schedulesList)

                binding.scheduleListView.adapter = schedulesListAdapter
            }
    }

    private fun setupSchedulesRecyclerView() {
        getScheduleListFromDb(
            binding.calendarView.getSelectedMonthAsStr(),
            binding.calendarView.getSelectedYearAsStr()
        )
        binding.calendarView.setMonthChangeListener(object : CalendarView.DataChangeListener {
            override fun onDataChanged(selectedMonth: String, selectedYear: String) {
                getScheduleListFromDb(selectedMonth, selectedYear)
            }
        })

        var listIsCollapsed = true
        binding.scheduleListView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val verticalScrollOffset = recyclerView.computeVerticalScrollOffset()
                    // Равен нулю, в случае когда смещения по вертикали не происходит,
                    // соответственно мы в верху списка и нужно свернуть список
                    if (verticalScrollOffset == 0) {
                        // Скролла нет
                        if (!listIsCollapsed) {
                            // Обработка случая, когда нет места для скролла вверх
                            binding.calendarView.fillCalendarWithMonth(
                                binding.calendarView.getSelectedYearAsInt(),
                                binding.calendarView.getSelectedMonthAsInt()
                            )
                            listIsCollapsed = true
                        }
                    } else if (verticalScrollOffset > 0) {
                        // Скролл вниз
                        if (listIsCollapsed) {
                            binding.calendarView.fillCalendarWithWeek()
                            listIsCollapsed = false
                        }
                    }
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        scheduleDao = MainDb.getDb(this).getScheduleDao()
        //binding.scheduleListView.setHasFixedSize(true)
        binding.scheduleListView.layoutManager = LinearLayoutManager(this)

        setupSchedulesRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.calendarView.removeMonthChangeListener()
    }
}
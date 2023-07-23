package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ActivityScheduleListBinding
import com.google.android.material.snackbar.Snackbar
import models.ScheduleModel
import java.util.Timer
import kotlin.concurrent.timerTask

class ScheduleListActivity : AppCompatActivity() {

    private lateinit var schedulesList: ArrayList<ScheduleModel>
    private lateinit var schedulesListAdapter: ScheduleAdapter
    private lateinit var scheduleDao: ScheduleDao
    private lateinit var binding: ActivityScheduleListBinding
    private var listIsCollapsed = true

    private fun getDaysNumFromListSchedules(schedulesList: List<ScheduleModel>): List<Int> {
        val numsOfList: ArrayList<Int> = ArrayList()
        schedulesList.forEach { schedule ->
            numsOfList.add(schedule.startDateTime.substring(4, 6).toInt())
        }

        return numsOfList.toList()
    }

    private fun getScheduleListFromDb(selectedMonth: String, selectedYear: String) {
        scheduleDao.getMonthSchedules("$selectedMonth $selectedYear").asLiveData()
            .observe(this@ScheduleListActivity) { dbSchedulesList ->
                schedulesList = ArrayList(dbSchedulesList)
                schedulesListAdapter = ScheduleAdapter(this, schedulesList)
                schedulesListAdapter.onItemClick = { scheduleItem ->
                    val intent = Intent(this, ScheduleActivity::class.java)
                    intent.putExtra("idSchedule", scheduleItem.id)
                    startActivity(intent)
                }

                binding.calendarView.setupDaysWithSchedules(
                    getDaysNumFromListSchedules(
                        schedulesList
                    )
                )

                if (dbSchedulesList.isEmpty()) {
                    binding.schuduleListMissingMsg.visibility = View.VISIBLE
                } else {
                    binding.schuduleListMissingMsg.visibility = View.GONE
                }
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

                listIsCollapsed = true
            }
        })

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
                            // если был свёрнут
                            if (!listIsCollapsed) {
                                binding.calendarView.setupDaysWithSchedules()
                            }
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

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val durationUndoPossibility = 3000
                val position = viewHolder.adapterPosition
                val deletedSchedule = schedulesList[position]

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                schedulesList.removeAt(position)

                // below line is to notify our item is removed from adapter.
                schedulesListAdapter.notifyItemRemoved(position)

                var scheduleIsDeleted = true

                // below line is to display our snack-bar with action.
                Snackbar.make(
                    binding.scheduleListView,
                    "Deleted " + if (deletedSchedule.title != "") (deletedSchedule.title) else ("anonymous"),
                    Snackbar.LENGTH_LONG
                )
                    .setDuration(durationUndoPossibility)
                    .setAction(
                        "Undo"
                    ) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        schedulesList.add(position, deletedSchedule)

                        // below line is to notify item is
                        // added to our adapter class.
                        schedulesListAdapter.notifyItemInserted(position)

                        scheduleIsDeleted = false
                    }.show()

                Timer().schedule(timerTask {
                    if (scheduleIsDeleted) {
                        Thread {
                            scheduleDao.deleteSchedule(deletedSchedule)
                        }.start()
                    }
                }, durationUndoPossibility.toLong())
            }
        }).attachToRecyclerView(binding.scheduleListView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScheduleListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        scheduleDao = MainDb.getDb(this).getScheduleDao()
        binding.scheduleListView.layoutManager = LinearLayoutManager(this)

        setupSchedulesRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()

        binding.calendarView.removeMonthChangeListener()
    }
}
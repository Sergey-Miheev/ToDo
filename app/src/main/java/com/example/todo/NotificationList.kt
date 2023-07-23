package com.example.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.databinding.ActivityNotificationListBinding

class NotificationList : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationListBinding

    private fun setupNotificationList() {
        binding.notificationActivityList.layoutManager = LinearLayoutManager(this)

        val dbDao = MainDb.getDb(this).getScheduleDao()
        dbDao.getAllSchedules().asLiveData().observe(this) {dbNotificationList ->

            val adapter = NotificationAdapter(dbNotificationList)
            binding.notificationActivityList.adapter = adapter

            adapter.onItemClick = {notificationItem ->
                intent = Intent(this, ScheduleActivity::class.java)
                intent.putExtra("idSchedule", notificationItem.id)
                startActivity(intent)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupNotificationList()
    }
}
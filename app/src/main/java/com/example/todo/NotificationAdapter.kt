package com.example.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import models.ScheduleModel

class NotificationAdapter(inputNotificationList: List<ScheduleModel>): RecyclerView.Adapter<NotificationViewHolder>() {
    private var notificationList = inputNotificationList
    var onItemClick: ((ScheduleModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notification_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]

        holder.title.text = notification.title
        holder.date.text = notification.startDateTime.substring(4, 23)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(notification)
        }
    }
}
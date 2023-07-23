package com.example.todo

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById(R.id.notificationItem_title)
    var date: TextView = itemView.findViewById(R.id.notificationItem_date)
}
package com.example.todo

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var isClosed: CheckBox = itemView.findViewById(R.id.cardHeader_isClosed)
    var title: TextView = itemView.findViewById(R.id.cardHeader_title)
    var time: TextView = itemView.findViewById(R.id.scheduleContent_timeData)
    var place: TextView = itemView.findViewById(R.id.scheduleContent_placeData)
    var notes: TextView = itemView.findViewById(R.id.scheduleContent_notesData)
}

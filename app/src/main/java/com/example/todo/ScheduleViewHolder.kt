package com.example.todo

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var isClosed: CheckBox = itemView.findViewById(R.id.cardHeader_isClosed)
    var title: TextView = itemView.findViewById(R.id.cardHeader_title)
    var line: View = itemView.findViewById(R.id.scheduleCard_line)
    var timeData: TextView = itemView.findViewById(R.id.scheduleContent_timeData)
    var timeHeader: TextView = itemView.findViewById(R.id.scheduleContent_time)
    var placeData: TextView = itemView.findViewById(R.id.scheduleContent_placeData)
    var placeHeader: TextView = itemView.findViewById(R.id.scheduleContent_place)
    var notesData: TextView = itemView.findViewById(R.id.scheduleContent_notesData)
    var notesHeader: TextView = itemView.findViewById(R.id.scheduleContent_notes)
}
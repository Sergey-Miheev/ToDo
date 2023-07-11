package com.example.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import models.ScheduleModel

class ScheduleAdapter(inputSchedulesList: List<ScheduleModel>): RecyclerView.Adapter<ScheduleViewHolder>() {
    private var schedulesList = ArrayList(inputSchedulesList)
    var onItemClick: ((ScheduleModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.schedule_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return schedulesList.size
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val schedule = schedulesList[position]

        holder.title.text = schedule.title
        holder.isClosed.isChecked = schedule.isClosed
        var time = ""
        if (schedule.startDateTime != "") {
            time = schedule.startDateTime.substring(17).lowercase()
        }
        if (schedule.finishDateTime != "" && schedule.isClosed) {
            time += " - "
            time += schedule.finishDateTime.substring(17).lowercase()
        }
        holder.time.text = time
        holder.place.text = schedule.place
        holder.notes.text = schedule.notes

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(schedule)
        }
    }
}
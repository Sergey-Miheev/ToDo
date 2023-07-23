package com.example.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import models.ScheduleModel

class ScheduleAdapter(private val context: Context, inputSchedulesList: List<ScheduleModel>): RecyclerView.Adapter<ScheduleViewHolder>() {
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
        holder.timeData.text = time
        holder.placeData.text = schedule.place
        holder.notesData.text = schedule.notes

        if (schedule.isClosed) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.black_purple))
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.white_gray))
            holder.line.setBackgroundColor(ContextCompat.getColor(context, R.color.black_lilac))
            holder.timeData.setTextColor(ContextCompat.getColor(context, R.color.white_gray))
            holder.placeData.setTextColor(ContextCompat.getColor(context, R.color.white_gray))
            holder.notesData.setTextColor(ContextCompat.getColor(context, R.color.white_gray))
            holder.timeHeader.setTextColor(ContextCompat.getColor(context, R.color.white_gray))
            holder.placeHeader.setTextColor(ContextCompat.getColor(context, R.color.white_gray))
            holder.notesHeader.setTextColor(ContextCompat.getColor(context, R.color.white_gray))
        }


        holder.itemView.setOnClickListener {
            onItemClick?.invoke(schedule)
        }
    }
}
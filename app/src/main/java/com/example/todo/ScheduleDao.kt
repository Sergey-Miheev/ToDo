package com.example.todo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import models.ScheduleModel

@Dao
interface ScheduleDao {
    @Insert
    fun insertSchedule(schedule: ScheduleModel)
    @Delete
    fun deleteSchedule(schedule: ScheduleModel)
    @Update
    fun updateSchedule(schedule: ScheduleModel)
    @Query("SELECT * FROM schedule WHERE id == :idSchedule")
    fun getScheduleById(idSchedule: Int): Flow<ScheduleModel>
    @Query("SELECT * FROM schedule")
    fun getAllSchedules(): Flow<List<ScheduleModel>>
    @Query("SELECT * FROM schedule WHERE startDateTime LIKE '%' || :monthAndYear || '%'")
    fun getMonthSchedules(monthAndYear: String): Flow<List<ScheduleModel>>
}
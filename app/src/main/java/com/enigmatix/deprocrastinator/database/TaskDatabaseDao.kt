package com.enigmatix.deprocrastinator.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDatabaseDao {
    @Insert
    suspend fun insertTask(task: Task)
    @Insert
    suspend fun insertSubtask(subtask: Subtask)
    @Insert
    suspend fun insertXP(xp: XP)
    @Update
    suspend fun updateTask(task: Task)
    @Update
    suspend fun updateSubtask(subtask: Subtask)
    @Delete
    suspend fun deleteTask(task: Task)
    @Delete
    suspend fun deleteSubtask(subtask: Subtask)
    @Delete
    suspend fun deleteXP(xp: XP)
    @Query("select * from tasks")
    fun getTasks(): LiveData<List<Task>>
    @Query("select * from subtasks where taskId=:id")
    fun getSubtasks(id: Int): LiveData<List<Subtask>>
    @Query("select * from subtasks where taskID=:id and endDateTime is not null order by endDateTime limit 1")
    suspend fun getFirstDeadline(id: Int): Subtask?
    @Query("select * from xp")
    fun getXP(): LiveData<List<XP>>
}

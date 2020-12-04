package com.enigmatix.deprocrastinator.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDatabaseDao {
    @Insert
    suspend fun insertTask(task: Task)
    @Insert
    suspend fun insertSubtask(subtask: Subtask)
    @Update
    suspend fun updateTask(task: Task)
    @Update
    suspend fun updateSubtask(subtask: Subtask)
    @Delete
    suspend fun deleteTask(task: Task)
    @Delete
    suspend fun deleteSubtask(subtask: Subtask)
    @Query("select * from tasks")
    suspend fun getTasks(): LiveData<List<Task>>
    @Query("select * from subtasks where taskId=:id")
    suspend fun getSubtasks(id: Int): LiveData<List<Subtask>>
}

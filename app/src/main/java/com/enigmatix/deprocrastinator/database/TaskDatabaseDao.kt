package com.enigmatix.deprocrastinator.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDatabaseDao {
    @Insert
    suspend fun insertTask(task: Task)
    @Insert
    suspend fun insertSubtask(subtask: Subtask)
    @Insert(onConflict = OnConflictStrategy.REPLACE) //If foreign keys are looking at xp it will trigger onDelete on them
    suspend fun insertXP(dbXp: DbXP)
    @Update
    suspend fun updateTask(task: Task)
    @Update
    suspend fun updateSubtask(subtask: Subtask)
    @Delete
    suspend fun deleteTask(task: Task)
    @Delete
    suspend fun deleteSubtask(subtask: Subtask)
    @Delete
    suspend fun deleteXP(dbXp: DbXP)
    @Query("select * from tasks")
    fun getTasks(): LiveData<List<Task>>
    @Query("select * from tasks where id=:id")
    fun getTask(id: Int): LiveData<Task>
    @Query("select * from tasks where id=:id")
    suspend fun getTaskNow(id: Int): Task?
    @Query("select * from subtasks where id=:id")
    fun getSubtask(id: Int): LiveData<Subtask>
    @Query("select * from subtasks where taskId=:id and completed=1")
    fun getCompletedSubtasks(id: Int): LiveData<List<Subtask>>
    @Query("select * from subtasks where taskId=:id and completed=0")
    fun getIncompleteSubtasks(id: Int): LiveData<List<Subtask>>
    @Query("select * from subtasks where taskID=:id and endDateTime is not null and completed=0 order by endDateTime limit 1")
    suspend fun getFirstDeadline(id: Int): Subtask?
    @Query("select * from xp")
    fun getXP(): LiveData<List<DbXP>>
    @Query("select * from xp order by day desc limit 1")
    fun getLastXP(): LiveData<DbXP>
    @Query("select * from xp order by day desc limit 1")
    suspend fun getLastXPNow(): DbXP?
}

package com.enigmatix.deprocrastinator.addsubtask

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.Subtask
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao
import kotlinx.coroutines.*
import java.util.*

class AddSubtaskViewModel(private val database: TaskDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun addTask(taskId: Int, description: String, start: Date?, end: Date?, importance: Int, color: Int)  {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.insertSubtask(Subtask(
                    taskId = taskId,
                    description = description,
                    startDateTime = start,
                    endDateTime = end,
                    importance = importance,
                    color = color,
                    completed = 0
                ))
            }
        }
    }
}
package com.enigmatix.deprocrastinator.addsubtask

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.Subtask
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class AddSubtaskViewModel(private val database: TaskDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun addTask(taskId: Int, description: String, start: Date?, end: Date?, importance: Int, color: Int)  {
        uiScope.launch {
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
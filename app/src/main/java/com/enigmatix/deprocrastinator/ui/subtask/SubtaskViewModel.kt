package com.enigmatix.deprocrastinator.ui.subtask

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao
import kotlinx.coroutines.*

class SubtaskViewModel(private val taskId: Int, private val database: TaskDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val incompleteSubtasks = database.getIncompleteSubtasks(taskId)

    fun deleteTask() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val task = database.getTaskNow(taskId)
                if (task != null)
                    database.deleteTask(task)
            }
        }
    }
}
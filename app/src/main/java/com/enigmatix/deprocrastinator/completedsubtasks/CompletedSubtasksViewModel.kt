package com.enigmatix.deprocrastinator.completedsubtasks

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class CompletedSubtasksViewModel(private val taskId: Int, private val database: TaskDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val completedSubtasks = database.getCompletedSubtasks(taskId)
}
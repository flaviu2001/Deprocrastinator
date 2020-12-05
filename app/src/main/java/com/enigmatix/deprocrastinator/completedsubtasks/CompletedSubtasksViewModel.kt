package com.enigmatix.deprocrastinator.completedsubtasks

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class CompletedSubtasksViewModel(taskId: Int, database: TaskDatabaseDao) : ViewModel() {
    val completedSubtasks = database.getCompletedSubtasks(taskId)
}
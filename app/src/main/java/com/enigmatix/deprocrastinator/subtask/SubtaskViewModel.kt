package com.enigmatix.deprocrastinator.subtask

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class SubtaskViewModel(taskId: Int, database: TaskDatabaseDao) : ViewModel() {
    val subtasks = database.getSubtasks(taskId)
    val incompleteSubtasks = database.getIncompleteSubtasks(taskId)
}
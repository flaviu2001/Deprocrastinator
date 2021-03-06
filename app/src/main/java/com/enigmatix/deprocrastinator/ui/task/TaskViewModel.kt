package com.enigmatix.deprocrastinator.ui.task

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class TaskViewModel(database: TaskDatabaseDao) : ViewModel() {
    val taskList = database.getTasks()
}
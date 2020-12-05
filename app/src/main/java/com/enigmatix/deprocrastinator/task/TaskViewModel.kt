package com.enigmatix.deprocrastinator.task

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class TaskViewModel(database: TaskDatabaseDao) : ViewModel() {
    val taskList = database.getTasks()
}
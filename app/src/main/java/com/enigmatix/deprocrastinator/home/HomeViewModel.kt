package com.enigmatix.deprocrastinator.home

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class HomeViewModel(database: TaskDatabaseDao) : ViewModel() {
    val taskList = database.getTasks()
}
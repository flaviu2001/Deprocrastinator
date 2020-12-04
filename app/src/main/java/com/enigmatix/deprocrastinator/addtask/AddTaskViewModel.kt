package com.enigmatix.deprocrastinator.addtask

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.Task
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AddTaskViewModel(private val database: TaskDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun addTask(name: String, description: String, color: Int) {
        uiScope.launch {
            database.insertTask(Task(name = name, description = description, color = color))
        }
    }
}
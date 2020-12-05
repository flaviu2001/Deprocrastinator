package com.enigmatix.deprocrastinator.ui.addtask

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.Task
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao
import kotlinx.coroutines.*

class AddTaskViewModel(private val database: TaskDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun addTask(name: String, description: String, color: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.insertTask(Task(name = name, description = description, color = color))
            }
        }
    }
}
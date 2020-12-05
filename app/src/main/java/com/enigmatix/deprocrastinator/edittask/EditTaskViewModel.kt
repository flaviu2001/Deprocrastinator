package com.enigmatix.deprocrastinator.edittask

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.Task
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditTaskViewModel(taskId: Int, private val database: TaskDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val subtask = database.getTask(taskId)

    fun update(taskId: Int, name: String, description: String, color: Int) {
        uiScope.launch {
            database.updateTask(Task(taskId, name, description, color))
        }
    }
}
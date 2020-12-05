package com.enigmatix.deprocrastinator.editsubtask

import androidx.lifecycle.ViewModel
import com.enigmatix.deprocrastinator.database.Subtask
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao
import kotlinx.coroutines.*
import java.util.*

class EditSubtaskViewModel(id: Int, private val database: TaskDatabaseDao) : ViewModel() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val subtask = database.getSubtask(id)

    fun update(taskId: Int, description: String, start: Date?, end: Date?, importance: Int, color: Int, completed: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                database.updateSubtask(Subtask(
                    id=subtask.value!!.id,
                    taskId=taskId,
                    description=description,
                    startDateTime= start,
                    endDateTime= end,
                    importance=importance,
                    color=color,
                    completed = completed
                ))
            }
        }
    }

    fun deleteSubtask() {
        uiScope.launch {
            if (subtask.value != null)
                database.deleteSubtask(subtask.value!!)
        }
    }
}
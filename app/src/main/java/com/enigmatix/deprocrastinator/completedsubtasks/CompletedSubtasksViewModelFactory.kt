package com.enigmatix.deprocrastinator.completedsubtasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.addtask.AddTaskViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class CompletedSubtasksViewModelFactory (
    private val taskId: Int,
    private val dataSource: TaskDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletedSubtasksViewModel::class.java)) {
            return CompletedSubtasksViewModel(taskId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
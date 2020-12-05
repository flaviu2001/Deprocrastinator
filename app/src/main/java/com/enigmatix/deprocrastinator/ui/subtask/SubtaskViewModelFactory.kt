package com.enigmatix.deprocrastinator.ui.subtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class SubtaskViewModelFactory (
    private val id: Int,
    private val dataSource: TaskDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubtaskViewModel::class.java)) {
            return SubtaskViewModel(id, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
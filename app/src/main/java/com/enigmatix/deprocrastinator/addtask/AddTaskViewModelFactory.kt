package com.enigmatix.deprocrastinator.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class AddTaskViewModelFactory (
    private val dataSource: TaskDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            return AddTaskViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
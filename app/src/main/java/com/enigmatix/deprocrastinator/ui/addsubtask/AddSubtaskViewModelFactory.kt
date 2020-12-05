package com.enigmatix.deprocrastinator.ui.addsubtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class AddSubtaskViewModelFactory (private val dataSource: TaskDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSubtaskViewModel::class.java)) {
            return AddSubtaskViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.enigmatix.deprocrastinator.ui.edittask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class EditTaskViewModelFactory (private val id: Int, private val dataSource: TaskDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTaskViewModel::class.java)) {
            return EditTaskViewModel(id, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
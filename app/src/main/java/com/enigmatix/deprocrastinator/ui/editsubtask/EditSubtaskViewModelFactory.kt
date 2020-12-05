package com.enigmatix.deprocrastinator.ui.editsubtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class EditSubtaskViewModelFactory (private val id: Int, private val dataSource: TaskDatabaseDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditSubtaskViewModel::class.java)) {
            return EditSubtaskViewModel(id, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
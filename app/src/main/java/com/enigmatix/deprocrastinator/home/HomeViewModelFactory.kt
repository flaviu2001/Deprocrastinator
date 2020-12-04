package com.enigmatix.deprocrastinator.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.addtask.AddTaskViewModel
import com.enigmatix.deprocrastinator.database.TaskDatabaseDao

class HomeViewModelFactory (
    private val dataSource: TaskDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
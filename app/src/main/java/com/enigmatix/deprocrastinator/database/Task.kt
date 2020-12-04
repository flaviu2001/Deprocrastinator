package com.enigmatix.deprocrastinator.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task (
        @PrimaryKey (autoGenerate = true)
        var id: Int = 0,
        var name: String,
        var description: String,
        var color: Long
)

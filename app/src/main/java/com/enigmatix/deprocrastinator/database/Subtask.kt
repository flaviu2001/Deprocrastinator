package com.enigmatix.deprocrastinator.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "subtasks")
data class Subtask (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ForeignKey(onDelete = CASCADE, entity = Task::class, parentColumns = ["id"], childColumns = ["taskId"])
    var taskId: Int,
    var description: String,
    var startDateTime: Date?,
    var endDateTime: Date?,
    var importance: Int,
    var completed: Int,
    var color: Int
)

package com.enigmatix.deprocrastinator.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "xp")
data class XP (
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var day: Date,
        var xp: Long
)
package com.enigmatix.deprocrastinator.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "xp")
data class DbXP (
        @PrimaryKey(autoGenerate = false)
        var day: Date,
        var xp: Long
)
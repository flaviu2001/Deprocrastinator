@file:Suppress("DEPRECATION")

package com.enigmatix.deprocrastinator

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import com.enigmatix.deprocrastinator.database.DbXP
import com.enigmatix.deprocrastinator.database.TaskDatabase
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.roundToLong

object ExperienceManipulator {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun getCurrentXP(context: Context): LiveData<DbXP> {
        return TaskDatabase.getInstance(context).taskDatabaseDao.getLastXP()
    }

    fun getAllXP(context: Context): LiveData<List<DbXP>> {
        return TaskDatabase.getInstance(context).taskDatabaseDao.getAllXP()
    }

    fun addXP(activity: FragmentActivity, xp: Long) {
        val now = Date()
        val today = Date(now.year, now.month, now.date)
        val database = TaskDatabase.getInstance(activity.applicationContext).taskDatabaseDao
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        var multiplier = sharedPref.getFloat("multiplier", 1f)
        var day = sharedPref.getLong("day", today.time)
        val diff = kotlin.math.abs(day - today.time)
        if (diff < TimeUnit.HOURS.toMillis(25) && diff > TimeUnit.HOURS.toMillis(23))
            multiplier += 0.2f
        if (diff > TimeUnit.HOURS.toMillis(25))
            multiplier = 1f
        multiplier = min(multiplier, 5f)
        val addedXp = (multiplier*xp).roundToLong()
        day = today.time
        with(sharedPref.edit()) {
            putFloat("multiplier", multiplier)
            putLong("day", day)
            apply()
        }
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val currXP = database.getLastXPNow()?.xp ?: 0
                database.insertXP(DbXP(today, currXP+addedXp))
            }
        }


    }
}

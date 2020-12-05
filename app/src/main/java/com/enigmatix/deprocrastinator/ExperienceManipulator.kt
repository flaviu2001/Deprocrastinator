@file:Suppress("DEPRECATION")

package com.enigmatix.deprocrastinator

import android.content.Context
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

    fun addXP(activity: FragmentActivity, xp: Long) {
        val now = Date()
        val today = Date(now.year, now.month, now.year)
        val database = TaskDatabase.getInstance(activity.applicationContext).taskDatabaseDao
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        var multiplier = sharedPref.getFloat("multiplier", 1f)
        var day = sharedPref.getLong("day", today.time)
        if (kotlin.math.abs(day - Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)).time) < TimeUnit.HOURS.toMillis(1))
            multiplier += 0.2f
        if (kotlin.math.abs(day - Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)).time) > TimeUnit.HOURS.toMillis(25))
            multiplier = 1f
        multiplier = min(multiplier, 5f)

        day = today.time
        val addedXp = (multiplier*xp).roundToLong()
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

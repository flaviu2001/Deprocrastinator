@file:Suppress("DEPRECATION")

package com.enigmatix.deprocrastinator

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.notification.AlarmReceiver
import kotlinx.coroutines.*
import java.util.*

data class DateTime(
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int
)

fun prettyTimeString(date: Date?, noTime: Boolean = false): String {
    if (date == null)
        return ""
    var str1 = date.hours.toString()
    if (str1.length == 1)
        str1 = "0$str1"
    var str2 = date.minutes.toString()
    if (str2.length == 1)
        str2 = "0$str2"
    if (!noTime)
        return "${date.date}-${date.month+1}-${date.year+1900} $str1:$str2"
    return "${date.date}-${date.month+1}-${date.year+1900}"
}

fun prettyTimeString(datetime: DateTime): String {
    var str1 = datetime.hour.toString()
    if (str1.length == 1)
        str1 = "0$str1"
    var str2 = datetime.minute.toString()
    if (str2.length == 1)
        str2 = "0$str2"
    return "${datetime.day}-${datetime.month}-${datetime.year} $str1:$str2"
}

fun hideKeyboard(activity: Activity) {
    val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun scheduleNotification(
    activity: Activity,
    taskId: Int,
    description: String,
    importance: Int,
    id: Int,
    date: Date?
) {
    val viewModelJob = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    uiScope.launch {
        withContext(Dispatchers.IO) {
            val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val notificationIntent = Intent(activity.applicationContext, AlarmReceiver::class.java)
            if (date == null) {
                val notificationManager = activity.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id)
                return@withContext
            }
            val title = TaskDatabase.getInstance(activity).taskDatabaseDao.getTaskNow(taskId)!!.name
            notificationIntent.putExtra("title", title)
            notificationIntent.putExtra("description", "$description (${activity.resources.getStringArray(R.array.importances)[importance]})")
            notificationIntent.putExtra("id", id)
            val broadcast = PendingIntent.getBroadcast(
                activity.applicationContext,
                100,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.time, broadcast)
        }
    }
}
package com.enigmatix.deprocrastinator

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData

object UserInfoManipulator {
    val nameLiveData = MutableLiveData<String>()

    fun getName(activity: Activity): String? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val name = sharedPref.getString("name", null)
        if (name == null)
            nameLiveData.value = null
        else nameLiveData.value = name
        return name
    }

    fun setName(activity: Activity, name: String) {
        nameLiveData.value = name
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("name", name)
            apply()
        }
    }
}
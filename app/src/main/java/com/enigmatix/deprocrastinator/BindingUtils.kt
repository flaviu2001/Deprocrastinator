package com.enigmatix.deprocrastinator

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.enigmatix.deprocrastinator.database.Subtask
import com.enigmatix.deprocrastinator.database.Task
import com.enigmatix.deprocrastinator.database.TaskDatabase
import kotlinx.coroutines.*

@BindingAdapter("nameText")
fun TextView.setNameText(item: Task?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("descriptionText")
fun TextView.setDescriptionText(item: Task?) {
    item?.let {
        text = item.description
    }
}

@BindingAdapter("nextDeadlineText")
fun TextView.setNextDeadlineText(item: Task?) {
    item?.let {
        val job = Job()
        val scope = CoroutineScope(Dispatchers.Main + job)
        var subtask: Subtask?
        scope.launch {
            withContext(Dispatchers.IO) {
                subtask = TaskDatabase.getInstance(context).taskDatabaseDao.getFirstDeadline(item.id)
            }
            text = if (subtask == null)
                context.getString(R.string.no_deadline)
            else context.getString(R.string.yes_deadline).format(subtask!!.endDateTime)
        }
    }
}

@BindingAdapter("customBackgroundColor")
fun androidx.cardview.widget.CardView.setCustomBackgroundColor(item: Task?) {
    item?.let {
        setCardBackgroundColor(item.color.toInt())
    }
}

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
            else context.getString(R.string.yes_deadline).format(prettyTimeString(subtask!!.endDateTime))
        }
    }
}

@BindingAdapter("customBackgroundColor")
fun androidx.cardview.widget.CardView.setCustomBackgroundColor(item: Task?) {
    item?.let {
        setCardBackgroundColor(item.color)
    }
}

@BindingAdapter("description")
fun TextView.setDescription(item: Subtask?) {
    item?.let {
        text = item.description
    }
}

@BindingAdapter("importance")
fun TextView.setImportance(item: Subtask?) {
    item?.let {
        var newText = context.resources.getStringArray(R.array.importances)[it.importance]
        if (item.completed == 1)
            newText += " (completed)"
        text = newText
    }
}

@BindingAdapter("startDate")
fun TextView.setStartDate(item: Subtask?) {
    item?.let {
        if (item.startDateTime == null)
            visibility = TextView.GONE
        else {
            visibility = TextView.VISIBLE
            text = context.getString(R.string.starting_format).format(prettyTimeString(item.startDateTime))
        }
    }
}

@BindingAdapter("endDate")
fun TextView.setEndDate(item: Subtask?) {
    item?.let {
        if (item.endDateTime == null)
            visibility = TextView.GONE
        else {
            visibility = TextView.VISIBLE
            text = context.getString(R.string.finishing_format).format(prettyTimeString(item.endDateTime))
        }
    }
}

@BindingAdapter("customBackgroundColor")
fun androidx.cardview.widget.CardView.setCustomBackgroundColor(item: Subtask?) {
    item?.let {
        setCardBackgroundColor(item.color)
    }
}

package com.enigmatix.deprocrastinator.ui.task

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enigmatix.deprocrastinator.database.Task
import com.enigmatix.deprocrastinator.databinding.TaskBinding

class TaskAdapter(private val taskListener: TaskListener) : RecyclerView.Adapter<TaskHolder>() {
    var data = listOf<Task>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder.from(parent, taskListener)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class TaskHolder private constructor(private val binding: TaskBinding, private val taskListener: TaskListener): RecyclerView.ViewHolder(binding.root){
    fun bind(task: Task) {
        binding.task = task
        binding.clickListener = taskListener
    }

    companion object {
        fun from(parent: ViewGroup, taskListener: TaskListener): TaskHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TaskBinding.inflate(layoutInflater, parent, false)
            return TaskHolder(binding, taskListener)
        }
    }
}

class TaskListener(val clickListener: (taskId: Int) -> Unit) {
    fun onClick(task: Task) = clickListener(task.id)
}
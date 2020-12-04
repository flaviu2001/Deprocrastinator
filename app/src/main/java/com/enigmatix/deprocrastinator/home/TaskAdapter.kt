package com.enigmatix.deprocrastinator.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enigmatix.deprocrastinator.database.Task
import com.enigmatix.deprocrastinator.databinding.TaskBinding

class TaskAdapter : RecyclerView.Adapter<TaskHolder>() {
    var data = listOf<Task>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class TaskHolder private constructor(private val binding: TaskBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(task: Task) {
        binding.task = task
    }

    companion object {
        fun from(parent: ViewGroup): TaskHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = TaskBinding.inflate(layoutInflater, parent, false)
            return TaskHolder(binding)
        }
    }
}
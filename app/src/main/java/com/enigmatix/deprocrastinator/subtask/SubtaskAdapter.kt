package com.enigmatix.deprocrastinator.subtask

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.enigmatix.deprocrastinator.database.Subtask
import com.enigmatix.deprocrastinator.databinding.SubtaskBinding

class SubtaskAdapter : RecyclerView.Adapter<SubtaskHolder>() {
    var data = listOf<Subtask>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtaskHolder {
        return SubtaskHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SubtaskHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class SubtaskHolder private constructor(private val binding: SubtaskBinding): RecyclerView.ViewHolder(binding.root){
    fun bind(subtask: Subtask) {
        binding.subtask = subtask
    }

    companion object {
        fun from(parent: ViewGroup): SubtaskHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SubtaskBinding.inflate(layoutInflater, parent, false)
            return SubtaskHolder(binding)
        }
    }
}
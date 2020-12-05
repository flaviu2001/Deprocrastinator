package com.enigmatix.deprocrastinator.ui.completedsubtasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.CompletedSubtasksFragmentBinding
import com.enigmatix.deprocrastinator.ui.subtask.SubtaskAdapter
import com.enigmatix.deprocrastinator.ui.subtask.SubtaskListener

class CompletedSubtasksFragment : Fragment() {
    private lateinit var binding: CompletedSubtasksFragmentBinding
    private lateinit var viewModel: CompletedSubtasksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CompletedSubtasksFragmentBinding.inflate(inflater)
        val taskId = CompletedSubtasksFragmentArgs.fromBundle(requireArguments()).taskId
        viewModel = ViewModelProvider(this, CompletedSubtasksViewModelFactory(taskId, TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(CompletedSubtasksViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val adapter = SubtaskAdapter(SubtaskListener {})
        binding.completedSubtaskList.adapter = adapter
        viewModel.completedSubtasks.observe(viewLifecycleOwner){
            adapter.data = it
        }
        return binding.root
    }
}
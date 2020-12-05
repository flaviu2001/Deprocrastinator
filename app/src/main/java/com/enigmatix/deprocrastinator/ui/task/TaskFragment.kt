package com.enigmatix.deprocrastinator.ui.task

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.enigmatix.deprocrastinator.R
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.TaskFragmentBinding

class TaskFragment : Fragment() {
    private lateinit var binding: TaskFragmentBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = TaskFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(TaskViewModel::class.java)
        binding.viewModel = taskViewModel
        val adapter = TaskAdapter(TaskListener { taskId ->
            this.findNavController().navigate(TaskFragmentDirections.actionNavHomeToSubtaskFragment(taskId))
        })
        binding.taskList.adapter = adapter
        setHasOptionsMenu(true)
        taskViewModel.taskList.observe(viewLifecycleOwner){
            adapter.data = it
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}
package com.enigmatix.deprocrastinator.subtask

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.enigmatix.deprocrastinator.R
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.SubtaskFragmentBinding

class SubtaskFragment : Fragment() {
    private lateinit var binding: SubtaskFragmentBinding
    private lateinit var viewModel: SubtaskViewModel
    var taskId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = SubtaskFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        taskId = SubtaskFragmentArgs.fromBundle(requireArguments()).taskId
        viewModel = ViewModelProvider(this,
        SubtaskViewModelFactory(taskId, TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(SubtaskViewModel::class.java)
        binding.viewModel = viewModel
        val adapter = SubtaskAdapter(SubtaskListener { subtaskId ->
            this.findNavController().navigate(SubtaskFragmentDirections.actionNavSubtaskToEditSubtaskFragment(subtaskId))
        })
        binding.subtaskList.adapter = adapter
        viewModel.incompleteSubtasks.observe(viewLifecycleOwner){
            if (it.size < 2)
                binding.subtaskList.layoutManager = LinearLayoutManager(requireContext())
            else binding.subtaskList.layoutManager = GridLayoutManager(requireContext(), 2)
            adapter.data = it
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.subtask_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.nav_add_subtask) {
            this.findNavController().navigate(SubtaskFragmentDirections.actionSubtaskFragmentToAddSubtaskFragment(taskId))
            return true
        }
        if (item.itemId == R.id.delete_task) {
            viewModel.deleteTask()
            this.findNavController().navigateUp()
            return true
        }
        if (item.itemId == R.id.nav_completed_subtasks) {
            this.findNavController().navigate(SubtaskFragmentDirections.actionNavSubtaskToNavCompletedSubtasks(taskId))
            return true
        }
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}
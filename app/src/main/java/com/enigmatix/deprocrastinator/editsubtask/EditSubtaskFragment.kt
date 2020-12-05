package com.enigmatix.deprocrastinator.editsubtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.EditSubtaskFragmentBinding

class EditSubtaskFragment : Fragment() {
    private lateinit var binding: EditSubtaskFragmentBinding
    private lateinit var viewModel: EditSubtaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditSubtaskFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, EditSubtaskViewModelFactory(TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(EditSubtaskViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

}
package com.enigmatix.deprocrastinator.addtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.enigmatix.deprocrastinator.databinding.AddTaskFragmentBinding

class AddTaskFragment : Fragment() {
    private lateinit var binding: AddTaskFragmentBinding
    private lateinit var viewModel: AddTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddTaskFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(AddTaskViewModel::class.java)
        binding.viewModel = viewModel

        return binding.root
    }

}
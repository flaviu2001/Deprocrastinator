package com.enigmatix.deprocrastinator.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.enigmatix.deprocrastinator.R
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        homeViewModel = ViewModelProvider(this, HomeViewModelFactory(TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        val adapter = TaskAdapter()
        binding.taskList.adapter = adapter
        setHasOptionsMenu(true)
        homeViewModel.taskList.observe(viewLifecycleOwner){
            adapter.data = it
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        )
                || super.onOptionsItemSelected(item)
    }
}
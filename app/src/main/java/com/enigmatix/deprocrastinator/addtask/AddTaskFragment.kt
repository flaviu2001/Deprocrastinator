package com.enigmatix.deprocrastinator.addtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigmatix.deprocrastinator.R
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.AddTaskFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener

class AddTaskFragment : Fragment() {
    private lateinit var binding: AddTaskFragmentBinding
    private lateinit var viewModel: AddTaskViewModel
    var itemColor = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddTaskFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this, AddTaskViewModelFactory(TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(AddTaskViewModel::class.java)
        binding.viewModel = viewModel
        itemColor = requireContext().getColor(R.color.yellow_500)
        binding.colorEdit.setOnClickListener{
            val colorPickerDialog = ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setColor(itemColor)
                .create()
            colorPickerDialog.setColorPickerDialogListener(object: ColorPickerDialogListener {
                override fun onColorSelected(dialogId: Int, color: Int) {
                    itemColor = color
                    binding.colorEdit.setTextColor(color)
                }
                override fun onDialogDismissed(dialogId: Int) {

                }
            })
            colorPickerDialog.show(childFragmentManager, "Choose a color")
        }
        binding.button.setOnClickListener{
            if (binding.nameEdit.text.toString().isEmpty() || binding.descriptionEdit.text.toString().isEmpty()) {
                Snackbar.make(requireView(), requireContext().getString(R.string.empty_error), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addTask(binding.nameEdit.text.toString(), binding.descriptionEdit.text.toString(), itemColor)
            this.findNavController().navigateUp()
        }
        return binding.root
    }

}
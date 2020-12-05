package com.enigmatix.deprocrastinator.edittask

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.enigmatix.deprocrastinator.R
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.EditTaskFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener

class EditTaskFragment : Fragment() {
    private lateinit var binding: EditTaskFragmentBinding
    private lateinit var viewModel: EditTaskViewModel
    private var itemColor = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = EditTaskFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val taskId = EditTaskFragmentArgs.fromBundle(requireArguments()).taskId
        viewModel = ViewModelProvider(this, EditTaskViewModelFactory(taskId, TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(EditTaskViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.subtask.observe(viewLifecycleOwner){
            if (it != null) {
                binding.nameEdit.setText(it.name)
                binding.descriptionEdit.setText(it.description)
                binding.colorEdit.setTextColor(it.color)
                itemColor = it.color
            }
        }
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
        binding.button.setOnClickListener {
            if (binding.nameEdit.text.toString().isEmpty() || binding.descriptionEdit.text.toString().isEmpty()) {
                Snackbar.make(requireView(), requireContext().getString(R.string.empty_error), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.update(taskId, binding.nameEdit.text.toString(), binding.descriptionEdit.text.toString(), itemColor)
            this.findNavController().navigateUp()
        }
        return binding.root
    }

}
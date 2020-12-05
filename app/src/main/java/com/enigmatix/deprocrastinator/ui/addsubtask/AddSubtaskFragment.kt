package com.enigmatix.deprocrastinator.ui.addsubtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigmatix.deprocrastinator.DateTime
import com.enigmatix.deprocrastinator.R
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.AddSubtaskFragmentBinding
import com.enigmatix.deprocrastinator.prettyTimeString
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import java.util.*

class AddSubtaskFragment : Fragment() {
    private lateinit var binding: AddSubtaskFragmentBinding
    private lateinit var viewModel: AddSubtaskViewModel
    private var start: DateTime? = null
    private var end: DateTime? = null
    private var case = -1
    private var itemColor = 0
    private var importance = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddSubtaskFragmentBinding.inflate(inflater)
        viewModel = ViewModelProvider(this, AddSubtaskViewModelFactory(TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(AddSubtaskViewModel::class.java)
        binding.viewModel = viewModel
        itemColor = requireContext().getColor(R.color.darker_yellow)
        val taskId = AddSubtaskFragmentArgs.fromBundle(requireArguments()).taskId
        binding.importanceEdit.setOnClickListener{
            var selectedItem: Int? = null
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose")
                .setSingleChoiceItems(R.array.importances, -1) { _, which ->
                    selectedItem = which
                    importance = which
                }
                .setPositiveButton("Ok") { _, _ ->
                    if (selectedItem != null)
                        binding.importanceEdit.setText(resources.getStringArray(R.array.importances)[selectedItem!!])
                }
                .setNegativeButton("Cancel") { _, _ ->
                    return@setNegativeButton
                }
            builder.create()
            builder.show()
        }
        binding.deadlineChoose.setOnClickListener{
            var selectedItem: Int? = null
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choose")
                .setSingleChoiceItems(R.array.schedules, -1) { _, which ->
                    selectedItem = which
                }
                .setPositiveButton("Ok") { _, _ ->
                    if (selectedItem != null) {
                        binding.deadlineChoose.setText(resources.getStringArray(R.array.schedules)[selectedItem!!])
                        when(selectedItem) {
                            0 -> {
                                binding.startEdit.visibility = EditText.GONE
                                binding.endEdit.visibility = EditText.VISIBLE
                            }
                            1 -> {
                                binding.startEdit.visibility = EditText.GONE
                                binding.endEdit.visibility = EditText.GONE
                            }
                            else -> {
                                binding.startEdit.visibility = EditText.VISIBLE
                                binding.endEdit.visibility = EditText.VISIBLE
                            }
                        }
                        binding.startEdit.text.clear()
                        binding.endEdit.text.clear()
                        start = null
                        end = null
                        case = selectedItem!!
                    }
                }
                .setNegativeButton("Cancel") { _, _ ->
                    return@setNegativeButton
                }
            builder.create()
            builder.show()
        }
        binding.startEdit.setOnClickListener{
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    if (start == null)
                        start = DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0)
                    else {
                        start!!.let {
                            it.year = year
                            it.month = monthOfYear+1
                            it.day = dayOfMonth
                        }
                    }
                    val c = Calendar.getInstance()
                    TimePickerDialog(
                        requireContext(), { _, hourOfDay, minute ->
                            start!!.let {
                                it.hour = hourOfDay
                                it.minute = minute
                            }
                            binding.startEdit.setText(prettyTimeString(start!!))
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true).show()
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.endEdit.setOnClickListener{
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    if (end == null)
                        end = DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0)
                    else {
                        end!!.let {
                            it.year = year
                            it.month = monthOfYear+1
                            it.day = dayOfMonth
                        }
                    }
                    val c = Calendar.getInstance()
                    TimePickerDialog(
                        requireContext(), { _, hourOfDay, minute ->
                            end!!.let {
                                it.hour = hourOfDay
                                it.minute = minute
                            }
                            binding.endEdit.setText(prettyTimeString(end!!))
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),true).show()
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        binding.addButton.setOnClickListener{
            when(case) {
                -1 -> {
                    Snackbar.make(requireView(), requireContext().getString(R.string.subtask_error), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                0 -> {
                    if (end == null) {
                        Snackbar.make(requireView(), requireContext().getString(R.string.datetime_error), Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                2 -> {
                    if (start == null || end == null) {
                        Snackbar.make(requireView(), requireContext().getString(R.string.datetime_error), Snackbar.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
            }
            if (binding.descriptionEdit.text.isBlank()) {
                Snackbar.make(requireView(), requireContext().getString(R.string.subtask_error), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.importanceEdit.text.isBlank()) {
                Snackbar.make(requireView(), requireContext().getString(R.string.subtask_error), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var startDate: Date? = null
            if (start != null) {
                startDate = Date(start!!.year - 1900, start!!.month - 1, start!!.day, start!!.hour, start!!.minute)
                if (startDate < Date()) {
                    Snackbar.make(requireView(), requireContext().getString(R.string.time_in_past_error), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            var endDate: Date? = null
            if (end != null) {
                endDate = Date(end!!.year - 1900, end!!.month - 1, end!!.day, end!!.hour, end!!.minute)
                if (endDate < Date()) {
                    Snackbar.make(requireView(), requireContext().getString(R.string.time_in_past_error), Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            if (startDate != null && endDate != null && startDate > endDate) {
                Snackbar.make(requireView(), requireContext().getString(R.string.start_more_end_error), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addTask(taskId, binding.descriptionEdit.text.toString(), startDate, endDate, importance, itemColor)
            this.findNavController().navigateUp()
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
        return binding.root
    }
}
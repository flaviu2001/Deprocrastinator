@file:Suppress("DEPRECATION")

package com.enigmatix.deprocrastinator.ui.editsubtask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.enigmatix.deprocrastinator.*
import com.enigmatix.deprocrastinator.database.Subtask
import com.enigmatix.deprocrastinator.database.TaskDatabase
import com.enigmatix.deprocrastinator.databinding.EditSubtaskFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import kotlinx.android.synthetic.main.subtask.*
import kotlinx.android.synthetic.main.task_fragment.view.*
import java.util.*

class EditSubtaskFragment : Fragment() {
    private lateinit var binding: EditSubtaskFragmentBinding
    private lateinit var viewModel: EditSubtaskViewModel
    private var start: DateTime? = null
    private var end: DateTime? = null
    private var case = -1
    private var itemColor = 0
    private var importance = 0
    private var taskId = 0
    private var completed = 0
    private lateinit var subtask: Subtask


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideKeyboard(requireActivity())
        binding = EditSubtaskFragmentBinding.inflate(inflater)
        val subtaskId = EditSubtaskFragmentArgs.fromBundle(requireArguments()).subtaskId
        viewModel = ViewModelProvider(this, EditSubtaskViewModelFactory(subtaskId, TaskDatabase.getInstance(requireContext()).taskDatabaseDao)).get(EditSubtaskViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.subtask.observe(viewLifecycleOwner){
            if (it != null) {
                subtask = it
                binding.descriptionEdit.setText(it.description)
                binding.importanceEdit.setText(resources.getStringArray(R.array.importances)[it.importance])
                importance = it.importance
                itemColor = it.color
                binding.deadlineChoose.setText(resources.getStringArray(R.array.schedules)[1])
                case = 1
                taskId = it.taskId
                completed = it.completed
                if (it.endDateTime != null) {
                    binding.deadlineChoose.setText(resources.getStringArray(R.array.schedules)[0])
                    binding.endEdit.visibility = EditText.VISIBLE
                    binding.endEdit.setText(prettyTimeString(it.endDateTime))
                    end = DateTime(it.endDateTime!!.year+1900, it.endDateTime!!.month+1, it.endDateTime!!.date, it.endDateTime!!.hours, it.endDateTime!!.minutes)
                    case = 0
                }
                if (it.startDateTime != null) {
                    binding.deadlineChoose.setText(resources.getStringArray(R.array.schedules)[2])
                    binding.startEdit.visibility = EditText.VISIBLE
                    binding.startEdit.setText(prettyTimeString(it.startDateTime))
                    start = DateTime(it.startDateTime!!.year+1900, it.startDateTime!!.month+1, it.startDateTime!!.date, it.startDateTime!!.hours, it.startDateTime!!.minutes)
                    case = 2
                }
                binding.colorEdit.setTextColor(it.color)
            }
        }
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
                            it.month = monthOfYear + 1
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
                            it.month = monthOfYear + 1
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
        binding.completedButton.setOnClickListener{
            viewModel.update(subtask.taskId, subtask.description, subtask.startDateTime, subtask.endDateTime, subtask.importance, subtask.color, 1)
            ExperienceManipulator.addXP(requireActivity(), 10)
            this.findNavController().navigateUp()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_subtask_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_subtask) {
            when(case) {
                -1 -> {
                    Snackbar.make(requireView(), requireContext().getString(R.string.subtask_error), Snackbar.LENGTH_SHORT).show()
                    return false
                }
                0 -> {
                    if (end == null) {
                        Snackbar.make(requireView(), requireContext().getString(R.string.datetime_error), Snackbar.LENGTH_SHORT).show()
                        return false
                    }
                }
                2 -> {
                    if (start == null || end == null) {
                        Snackbar.make(requireView(), requireContext().getString(R.string.datetime_error), Snackbar.LENGTH_SHORT).show()
                        return false
                    }
                }
            }
            if (binding.descriptionEdit.text.isBlank()) {
                Snackbar.make(requireView(), requireContext().getString(R.string.subtask_error), Snackbar.LENGTH_SHORT).show()
                return false
            }
            if (binding.importanceEdit.text.isBlank()) {
                Snackbar.make(requireView(), requireContext().getString(R.string.subtask_error), Snackbar.LENGTH_SHORT).show()
                return false
            }
            var startDate: Date? = null
            if (start != null) {
                startDate = Date(start!!.year - 1900, start!!.month - 1, start!!.day, start!!.hour, start!!.minute)
                if (startDate < Date()) {
                    Snackbar.make(requireView(), requireContext().getString(R.string.time_in_past_error), Snackbar.LENGTH_SHORT).show()
                    return false
                }
            }
            var endDate: Date? = null
            if (end != null) {
                endDate = Date(end!!.year - 1900, end!!.month - 1, end!!.day, end!!.hour, end!!.minute)
                if (endDate < Date()) {
                    Snackbar.make(requireView(), requireContext().getString(R.string.time_in_past_error), Snackbar.LENGTH_SHORT).show()
                    return false
                }
            }
            if (startDate != null && endDate != null && startDate > endDate) {
                Snackbar.make(requireView(), requireContext().getString(R.string.start_more_end_error), Snackbar.LENGTH_SHORT).show()
                return false
            }
            viewModel.update(taskId, binding.descriptionEdit.text.toString(), startDate, endDate, importance, itemColor, completed)
            this.findNavController().navigateUp()
            return true
        }
        if (item.itemId == R.id.delete_subtask) {
            viewModel.deleteSubtask()
            this.findNavController().navigateUp()
            return true
        }
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}
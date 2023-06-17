package com.android.kotlinmvvmtodolist.ui.update

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.data.local.TaskEntry
import com.android.kotlinmvvmtodolist.databinding.FragmentUpdateBinding
import com.android.kotlinmvvmtodolist.ui.task.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class UpdateFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateFragmentArgs>()
    private var dueTime: LocalTime? = null

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        binding.apply {
            //val taskEntry = TaskEntry
            updateNamaTugasInp.setText(args.task.title)
            updateDeskTugasInp.setText(args.task.desk)
            updateSpinner.setSelection(args.task.priority)
            updateSetTimeButton.setText(args.task.dueTimeString)
            val time = args.task.timestamp
            val timeString = convertTimeStamp(time)
            taskTimestamp.text = timeString
            checkboxItem.isChecked = args.task.isCompleted


            btnUpdate.setOnClickListener {
                val nama = updateNamaTugasInp.text.toString()
                val desk = updateDeskTugasInp.text.toString()
                val complete = if (checkboxItem.isChecked) true else false
                val dueTimeString =
                    if (dueTime == null) args.task.dueTimeString else dueTime.toString()
                if (nama.isBlank() || desk.isBlank()) {
                    Toast.makeText(requireContext(), "Harap isi Dengan Benar", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                //val taskTitle = updateEdtTask.text.toString()
                val priority = updateSpinner.selectedItemPosition

                val updateTask = TaskEntry(
                    args.task.id,
                    nama,
                    desk,
                    priority,
                    args.task.timestamp,
                    dueTimeString,
                    complete
                )

                viewModel.update(updateTask)
                Toast.makeText(requireContext(), "Updated!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_updateFragment_to_taskFragment)
            }
            updateSetTimeButton.setOnClickListener {
                openTimePicker()
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openTimePicker() {
        if (dueTime == null)
            dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButton()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()
    }

    private fun updateTimeButton() {
        binding.updateSetTimeButton.text =
            String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

    private fun convertTimeStamp(timeStamp: Long): String {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(Date(timeStamp))
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.task_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> shareTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareTask() {
        val nama = binding.updateNamaTugasInp.text
        val desk = binding.updateDeskTugasInp.text
        val complete = if (binding.checkboxItem.isChecked) "Selesai" else "Belum Selesai"
        val timeStamp = binding.taskTimestamp.text
        val dueTime = binding.updateSetTimeButton.text

        val message = getString(
            R.string.bagikan_template,
            nama,
            desk,
            timeStamp,
            dueTime,
            complete
        )
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
        if (shareIntent.resolveActivity(
                requireActivity().packageManager) != null) {
            startActivity(shareIntent)
        }
    }

}
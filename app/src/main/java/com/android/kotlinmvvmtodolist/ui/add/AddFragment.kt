package com.android.kotlinmvvmtodolist.ui.add

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.data.local.TaskEntry
import com.android.kotlinmvvmtodolist.databinding.FragmentAddBinding
import com.android.kotlinmvvmtodolist.ui.task.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime

@AndroidEntryPoint
class AddFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private var dueTime: LocalTime? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddBinding.inflate(inflater, container, false)

        val myAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.priorities)
        )

        binding.apply {
            spinner.adapter = myAdapter
            //val taskEntry = TaskEntry

            btnAdd.setOnClickListener {
                val nama = namaTugasInp.text.toString()
                val desk = deskTugasInp.text.toString()
                val dueTimeString = if (dueTime == null) null else dueTime.toString()
                if(nama.isBlank() || desk.isBlank()){
                    Toast.makeText(requireContext(), "Harap isi dengan benar!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                Log.d("cek isBlank", "nama: $nama dan desk: $desk , dan time: $dueTimeString")
                //val titleTitle = edtTask.text.toString()
                val priority = spinner.selectedItemPosition

                val tambahTask = TaskEntry(
                    0,
                    nama,
                    desk,
                    priority,
                    System.currentTimeMillis(),
                    dueTimeString,
                    false
                )

                viewModel.insert(tambahTask)
                Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_addFragment_to_taskFragment)
            }
            setTimeButton.setOnClickListener {
                openTimePicker()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun openTimePicker() {
        if (dueTime==null)
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
        binding.setTimeButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

}
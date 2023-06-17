package com.android.kotlinmvvmtodolist.ui.task

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.kotlinmvvmtodolist.data.local.TaskEntry
import com.android.kotlinmvvmtodolist.databinding.RowLayoutBinding

class TaskAdapter(
    private val clickListener: TaskClickListener
):
    ListAdapter<TaskEntry, TaskAdapter.ViewHolder>(TaskDiffCallback) {


    companion object TaskDiffCallback : DiffUtil.ItemCallback<TaskEntry>(){
        override fun areItemsTheSame(oldItem: TaskEntry, newItem: TaskEntry) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TaskEntry, newItem: TaskEntry) = oldItem == newItem
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        if(current != null){
            holder.bind(current, clickListener)
        }
    }


    inner class ViewHolder(private val binding: RowLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(taskEntry: TaskEntry, clickListener: TaskClickListener) {
            binding.taskEntry = taskEntry
            binding.clickListener = clickListener
            binding.executePendingBindings()
            //binding.checkboxItem.isChecked = taskEntry.isCompleted

            if (binding.checkboxItem.isChecked) {
                binding.taskTitle.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                binding.taskPriority.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                binding.taskTimestamp.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                binding.taskTitle.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                binding.taskPriority.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                binding.taskTimestamp.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }

            }
                //updateStatus(taskEntry.id, isChecked)
                Log.e("salah","id: ${taskEntry.id} dan isComplete: ${taskEntry.isCompleted}")



        }

//        private fun updateStatus(id: Int, isCompleted: Boolean){
//            CoroutineScope(Dispatchers.IO).launch {
//                taskRepository.updateStatus(id, isCompleted)
//            }
//        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


}

class TaskClickListener(val clickListener: (taskEntry: TaskEntry) -> Unit){
    fun onClick(taskEntry: TaskEntry) = clickListener(taskEntry)
}

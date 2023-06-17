package com.android.kotlinmvvmtodolist.ui.task

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.android.kotlinmvvmtodolist.data.TaskRepository
import com.android.kotlinmvvmtodolist.data.local.TaskEntry
import com.android.kotlinmvvmtodolist.network.UpdateWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository : TaskRepository
) : ViewModel(){

    val getAllTasks = repository.getAllTasks()
    val getAllPriorityTasks = repository.getAllPriorityTasks()

    fun insert(taskEntry: TaskEntry) = viewModelScope.launch {
        repository.insert(taskEntry)
    }

    fun delete(taskEntry: TaskEntry) = viewModelScope.launch{
            repository.deleteItem(taskEntry)
    }

    fun update(taskEntry: TaskEntry) = viewModelScope.launch{
        repository.updateData(taskEntry)
    }

    fun deleteAll() = viewModelScope.launch{
        repository.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<TaskEntry>> {
        return repository.searchDatabase(searchQuery)
    }
    fun scheduleUpdater(app: Application) {
        val request = OneTimeWorkRequestBuilder<UpdateWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(app).enqueueUniqueWork(
            UpdateWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

//    fun onTaskCheckedChanged(taskEntry: TaskEntry, isChecked: Boolean) = viewModelScope.launch {
//        repository.updateStatus(taskEntry.id, isChecked)
//    }

}
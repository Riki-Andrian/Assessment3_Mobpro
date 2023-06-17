package com.android.kotlinmvvmtodolist.data

import androidx.lifecycle.LiveData
import com.android.kotlinmvvmtodolist.data.local.TaskDao
import com.android.kotlinmvvmtodolist.data.local.TaskEntry
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    suspend fun insert(taskEntry: TaskEntry) = taskDao.insert(taskEntry)

    suspend fun updateData(taskEntry: TaskEntry) = taskDao.update(taskEntry)

    suspend fun deleteItem(taskEntry: TaskEntry) = taskDao.delete(taskEntry)

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }
    suspend fun updateStatus(id: Int, isCompleted: Boolean) = taskDao.updateTaskCompletionStatus(id, isCompleted)

    fun getAllTasks() = taskDao.getAllTasks()

    fun getAllPriorityTasks() = taskDao.getAllPriorityTasks()

    fun searchDatabase(searchQuery: String): LiveData<List<TaskEntry>> {
        return taskDao.searchDatabase(searchQuery)
    }

}

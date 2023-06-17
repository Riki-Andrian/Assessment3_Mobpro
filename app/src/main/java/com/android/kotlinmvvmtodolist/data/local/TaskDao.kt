package com.android.kotlinmvvmtodolist.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(taskEntry: TaskEntry)

    @Delete
    suspend fun delete(taskEntry: TaskEntry)

    @Update
    suspend fun update(taskEntry: TaskEntry)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table ORDER BY timestamp DESC")
    fun getAllTasks(): Flow<List<TaskEntry>>

    @Query("SELECT * FROM task_table ORDER BY priority ASC")
    fun getAllPriorityTasks(): Flow<List<TaskEntry>>

    @Query("SELECT * FROM task_table WHERE title LIKE :searchQuery ORDER BY timestamp DESC")
    fun searchDatabase(searchQuery: String): LiveData<List<TaskEntry>>

    @Query("UPDATE task_table SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateTaskCompletionStatus(id: Int, isCompleted: Boolean)
}
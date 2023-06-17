package com.android.kotlinmvvmtodolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TaskEntry::class], version = 1, exportSchema = false)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
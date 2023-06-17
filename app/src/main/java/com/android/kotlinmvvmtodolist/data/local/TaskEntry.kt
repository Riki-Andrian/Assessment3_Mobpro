package com.android.kotlinmvvmtodolist.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.kotlinmvvmtodolist.util.Constants.TASK_TABLE
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TASK_TABLE)
data class TaskEntry(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var desk: String,
    var priority: Int,
    var timestamp: Long,
    var dueTimeString: String?,
    var isCompleted: Boolean
):Parcelable
{
//    fun dueTime(): LocalTime? = if (dueTimeString == null)null
//    else LocalTime.parse(dueTimeString, timeFormater)
//
//    companion object{
//        val timeFormater: DateTimeFormatter = DateTimeFormatter.ISO_DATE
//    }

}
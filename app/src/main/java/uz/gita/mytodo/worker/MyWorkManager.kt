package uz.gita.mytodo.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import uz.gita.mytodo.data.model.TaskData
import uz.gita.mytodo.util.getCalendar
import java.util.concurrent.TimeUnit

fun Context.createWorkRequest(taskData: TaskData) {




    val calendar = getCalendar(taskData.timer)
    val currentTime = System.currentTimeMillis()

    val initialDelay = if (calendar.timeInMillis > currentTime) {
        calendar.timeInMillis - currentTime
    } else {
        0
    }

    if (initialDelay >= 0) {
        val data = Data.Builder().putLong(TASK_ID, taskData.id).build()
        val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        Log.d("TTT", "createWorkRequest: if")

        WorkManager.getInstance(this)
            .enqueueUniqueWork(taskData.getWorkerId(), ExistingWorkPolicy.REPLACE, workRequest)
    } else {
        cancelWorkRequest(taskData)
        Log.d("TTT", "createWorkRequest: else")
    }
}

fun Context.cancelWorkRequest(taskData: TaskData){
    WorkManager.getInstance(this).cancelUniqueWork(taskData.getWorkerId())
}
package uz.gita.mytodo.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import uz.gita.mytodo.R
import uz.gita.mytodo.data.local.dao.TaskDao

const val TASK_ID = "TASK_ID"

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted val params: WorkerParameters,
    private val taskDao: TaskDao,
    private val notificationManager: NotificationManager
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        Log.d("TTT", "createWorkRequest: do work")
        val remenderId = params.inputData.getLong(TASK_ID, -1)

        if (remenderId == -1L) return Result.failure()

        val task = taskDao.getTask(remenderId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("TTT", "createWorkRequest: build if")
            val channel = NotificationChannel(
                context.getString(R.string.remainder_channel_id),
                context.getString(R.string.remainder_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            channel.setBypassDnd(true)
            notificationManager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(
            context,
            context.getString(R.string.remainder_channel_id)
        )
            .apply {
                setContentTitle(task.title)
                setSmallIcon(R.mipmap.ic_launcher_round)
                setCategory(NotificationCompat.CATEGORY_ALARM)
                setContentText(task.body)
                setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                setDefaults(Notification.DEFAULT_SOUND)
                setDefaults(Notification.DEFAULT_VIBRATE)

                priority = NotificationCompat.PRIORITY_MAX
                setAutoCancel(true)
            }
        notificationManager.notify(task.id.toInt(), builder.build())
        taskDao.updateTask(task.apply {
            notified = true
        })

        return Result.success()

    }
}
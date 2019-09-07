package com.birthday.workmanger

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.birthday.ui.R

private const val CHANNEL_ID = "com.birthday.ui.CHANNEL_ID"

class NotifyWorker(val context: Context, val params: WorkerParameters) : Worker(context, params) {

  override fun doWork(): Result {
    // Method to trigger an instant notification
    triggerNotification()

    return Result.success()
    // (Returning RETRY tells WorkManager to try this task again
    // later; FAILURE says not to try again.)
  }

  private fun triggerNotification() {

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notification = Notification.Builder(context)
      .setSmallIcon(R.drawable.ic_birthday)
      .setContentTitle("Happy Birthday")
      .setContentText("Send Birthday Wishes")
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .build()

    notificationManager.notify(1,notification)
  }


}

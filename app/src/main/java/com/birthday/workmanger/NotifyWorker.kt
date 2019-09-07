package com.birthday.workmanger

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.birthday.ui.R
import com.birthday.ui.birthdayFeed.BirthdayFeedFragment

private const val CHANNEL_ID = "com.birthday.ui.CHANNEL_ID"

class NotifyWorker(val context: Context, val params: WorkerParameters) : Worker(context, params) {

  override fun doWork(): Result {
    // Method to trigger an instant notification
    showNotification("Birthday","Today Amit borthday")

    return Result.success()
    // (Returning RETRY tells WorkManager to try this task again
    // later; FAILURE says not to try again.)
  }

  fun showNotification(title: String, message: String) {
    val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      val channel = NotificationChannel(CHANNEL_ID,
        "Birthday",
        NotificationManager.IMPORTANCE_DEFAULT)
      channel.description = "Today Amit Birthday"
      mNotificationManager.createNotificationChannel(channel)
    }
    val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_birthday) // notification icon
      .setContentTitle(title) // title for notification
      .setContentText(message)// message for notification
      .setAutoCancel(true) // clear notification after click
    val intent = Intent(context, BirthdayFeedFragment::class.java)
    val pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    mBuilder.setContentIntent(pi)
    mNotificationManager.notify(0, mBuilder.build())
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

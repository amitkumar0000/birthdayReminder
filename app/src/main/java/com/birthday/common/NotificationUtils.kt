package com.birthday.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.birthday.ui.R
import com.birthday.ui.birthdayFeed.BirthdayFeedFragment

private const val CHANNEL_ID = "com.birthday.ui.CHANNEL_ID"

object NotificationUtils {
  fun showNotification(context:Context,title: String?, message: String?) {
    val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        "Birthday",
        NotificationManager.IMPORTANCE_DEFAULT)
      channel.description = "Birthday Message"
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
}
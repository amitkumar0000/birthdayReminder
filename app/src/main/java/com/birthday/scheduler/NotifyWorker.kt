package com.birthday.scheduler

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.birthday.common.NotificationUtils.showNotification
import com.birthday.ui.R
import com.birthday.ui.birthdayFeed.BirthdayFeedFragment


class NotifyWorker(val context: Context,val params: WorkerParameters) : Worker(context, params) {

  override fun doWork(): Result {
    // Method to trigger an instant notification
    val data = params.inputData
    showNotification(context,"Birthday","Today ${data.getString("name")} borthday")
    return Result.success()
    // (Returning RETRY tells WorkManager to try this task again
    // later; FAILURE says not to try again.)
  }





}

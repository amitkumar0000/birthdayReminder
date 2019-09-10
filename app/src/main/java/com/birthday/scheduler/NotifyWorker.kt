package com.birthday.scheduler

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.birthday.common.NotificationUtils.showNotification
import com.birthday.common.PrefenceManager

class NotifyWorker(val context: Context, val params: WorkerParameters) : Worker(context, params) {

  override fun doWork(): Result {
    // Method to trigger an instant notification
   var hashMap = PrefenceManager.loadHashMap(context)
    var name = params.inputData?.let { it.getString("name") }
    val timeInMillis = hashMap.get(name)?.let { it + 1000 * 60 * 60 * 24 * 365L }
    timeInMillis?.let {
      if (!name.isNullOrEmpty()) {
        hashMap.put(name, it)
        PrefenceManager.saveHashMap(context, hashMap)
      }

    }
    showNotification(context, "Birthday", "Today $name birthday")
    BirthdayWorkManager(context).startOneTimeWork()?.subscribe()
    return Result.success()
    // (Returning RETRY tells WorkManager to try this task again
    // later; FAILURE says not to try again.)
  }
}

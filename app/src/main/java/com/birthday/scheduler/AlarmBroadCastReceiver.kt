package com.birthday.scheduler

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.birthday.common.NotificationUtils.showNotification

class AlarmBroadCastReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context?, intent: Intent?) {
    if (context != null && intent != null) {
      showNotification(context, intent.getStringExtra("name"), intent.getStringExtra("message"))
    }
  }
}

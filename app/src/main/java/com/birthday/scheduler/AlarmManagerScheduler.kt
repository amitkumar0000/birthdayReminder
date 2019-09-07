package com.birthday.scheduler

import android.app.AlarmManager
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import com.birthday.ui.R
import timber.log.Timber
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmManagerScheduler @Inject constructor(val context: Context) {

  fun attachAlarmScheduler(name:String,dob: Date) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
    val intent = Intent(context, AlarmBroadCastReceiver::class.java)
    intent.setAction("android.alarm.receiver")
    intent.putExtra("name",name)
    intent.putExtra("message",context.getString(R.string.birthdayMessage,name))
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      100, intent, FLAG_CANCEL_CURRENT
    )

    val dobtimeInMillis = dob.time
    val timeInMillis = System.currentTimeMillis() + dob.month

    Timber.d("Time $dobtimeInMillis   $timeInMillis")

    if(SDK_INT < Build.VERSION_CODES.M) {
      alarmManager?.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    } else if (SDK_INT >= Build.VERSION_CODES.M) {
      alarmManager?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
        timeInMillis, pendingIntent); }

  }
}
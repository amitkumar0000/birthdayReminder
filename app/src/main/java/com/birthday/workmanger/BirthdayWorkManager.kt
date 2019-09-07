package com.birthday.workmanger

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class BirthdayWorkManager(
  private val repeatInterval: Long,
  private val intervalUnit: TimeUnit
) {

  fun startWorkManagerWithParams() {
    val data = Data.Builder()
      .build()
    val work = PeriodicWorkRequest.Builder(NotifyWorker::class.java, repeatInterval, intervalUnit)
      .setInputData(data)
      .setConstraints(constraints())
      .build()
    WorkManager.getInstance().enqueueUniquePeriodicWork("BirthdayWorker", ExistingPeriodicWorkPolicy.KEEP, work)
  }

  private fun constraints(): Constraints {
    return Constraints.Builder()
      .setRequiresBatteryNotLow(true)
      .build()
  }
}
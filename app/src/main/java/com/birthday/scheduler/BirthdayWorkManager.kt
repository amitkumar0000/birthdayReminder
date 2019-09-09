package com.birthday.scheduler

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.reactivex.Completable
import java.util.concurrent.TimeUnit

class BirthdayWorkManager(
  private val repeatInterval: Long = 1,
  private val intervalUnit: TimeUnit = TimeUnit.SECONDS
) {

  val hashMap = HashMap<String, Long>()

  fun startOneTimeWork(name: String, initialDelay: Long):Completable {
    if (hashMap.containsKey(name)) {
      hashMap.remove(name)
      hashMap.put(name, initialDelay)
    }

    val data = Data.Builder()
//      .putString("name", getSortedMap(name, initialDelay).entries.toTypedArray()[0].key)
      .build()
    val work = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
      .setInitialDelay(1, TimeUnit.MINUTES)
      .addTag("Birthday-Worker")
      .setInputData(data)
      .setConstraints(constraints())
      .build()
    WorkManager.getInstance().cancelAllWorkByTag("Birthday-Worker")
    return Completable.fromAction{
      WorkManager.getInstance().enqueue(work)
    }
  }

  private fun getSortedMap(name: String, initialDelay: Long): Map<String, Long> {

    return hashMap.toList().sortedBy { (_, value) -> value }.toMap()
  }

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
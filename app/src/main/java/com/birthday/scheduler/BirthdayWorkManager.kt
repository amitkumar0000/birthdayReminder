package com.birthday.scheduler

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.birthday.common.PrefenceManager
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BirthdayWorkManager @Inject constructor(val context: Context) {

  fun startOneTimeWork(): Completable? {
    var hashMap = PrefenceManager.loadHashMap(context)
    if(hashMap.isNotEmpty()) {

      return Completable.fromAction {

        val data = Data.Builder()
          .putString("name", getSortedMap(hashMap).entries.toTypedArray()[0].key)
          .build()
        val work = OneTimeWorkRequest.Builder(NotifyWorker::class.java)
          .setInitialDelay(1, TimeUnit.MINUTES)
          .addTag("Birthday-Worker")
          .setInputData(data)
          .setConstraints(constraints())
          .build()
        WorkManager.getInstance().cancelAllWorkByTag("Birthday-Worker")
        WorkManager.getInstance().enqueue(work)
      }.subscribeOn(Schedulers.io())
    }else{
      return null
    }
  }

  private fun getSortedMap(hashMap: HashMap<String, Long>): Map<String, Long> {

    return hashMap.toList().sortedBy { (_, value) -> value }.toMap()
  }

  fun startWorkManagerWithParams(repeatInterval: Long, intervalUnit: TimeUnit) {
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
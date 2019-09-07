package com.birthday

import android.app.Application
import com.birthday.ui.birthdayFeed.di.BirthdayStorageComponent
import com.birthday.ui.birthdayFeed.di.BirthdayStorageModule
import com.birthday.di.modules.ContextModule
import com.birthday.ui.birthdayFeed.di.DaggerBirthdayStorageComponent
import timber.log.Timber

class BirthdayApplication : Application() {

  companion object {
    lateinit var component: BirthdayStorageComponent
  }



  override fun onCreate() {
    super.onCreate()

    Timber.plant(Timber.DebugTree())

    component = DaggerBirthdayStorageComponent.builder().contextModule(ContextModule(this))
      .birthdayStorageModule(BirthdayStorageModule()).build()
  }

}
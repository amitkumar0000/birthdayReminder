package com.birthday

import android.app.Application
import com.birthday.ui.birthdayFeed.di.BirthdayStorageComponent
import com.birthday.ui.birthdayFeed.di.BirthdayStorageModule
import com.birthday.ui.birthdayFeed.di.ContextModule
import com.birthday.ui.birthdayFeed.di.DaggerBirthdayStorageComponent

class BirthdayApplication : Application() {

  companion object {
    lateinit var component: BirthdayStorageComponent
  }



  override fun onCreate() {
    super.onCreate()

    component = DaggerBirthdayStorageComponent.builder().contextModule(ContextModule(this))
      .birthdayStorageModule(BirthdayStorageModule()).build()
  }

}
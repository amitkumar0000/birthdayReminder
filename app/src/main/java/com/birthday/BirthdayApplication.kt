package com.birthday

import android.app.Application
import com.birthday.ui.birthdayFeed.di.BirthdayStorageComponent
import com.birthday.ui.birthdayFeed.di.BirthdayStorageModule
import com.birthday.di.modules.ContextModule
import com.birthday.ui.BuildConfig
import com.birthday.ui.birthdayFeed.di.DaggerBirthdayStorageComponent
import com.google.firebase.FirebaseApp
import timber.log.Timber
import io.fabric.sdk.android.Fabric
import com.crashlytics.android.core.CrashlyticsCore
import com.crashlytics.android.Crashlytics



class BirthdayApplication : Application() {

  companion object {
    lateinit var component: BirthdayStorageComponent
  }

  override fun onCreate() {
    super.onCreate()
    FirebaseApp.initializeApp(this);

    setupCrashlytics()

    if (BuildConfig.DEBUG)
      Timber.plant(Timber.DebugTree())

    component = DaggerBirthdayStorageComponent.builder().contextModule(ContextModule(this))
      .birthdayStorageModule(BirthdayStorageModule()).build()
  }

  fun setupCrashlytics(){
    // Set up Crashlytics, disabled for debug builds
    val crashlyticsKit = Crashlytics.Builder()
      .core(
        CrashlyticsCore.Builder()
          .build()
      )
      .build()

// Initialize Fabric with the debug-disabled Crashlytics
    Fabric.with(this, crashlyticsKit) // Correct initializer!
  }
}
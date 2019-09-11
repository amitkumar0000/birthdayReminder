package com.birthday.ui.birthdayFeed.di

import android.content.Context
import androidx.room.Room
import com.birthday.ui.birthdayFeed.BirthdayDao
import com.birthday.ui.birthdayFeed.BirthdayDatabase
import com.birthday.ui.birthdayFeed.BirthdayInteractor
import com.birthday.ui.birthdayFeed.BirthdayListManager
import com.birthday.ui.birthdayFeed.BirthdayRepository
import com.birthday.ui.birthdayFeed.DatabaseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BirthdayStorageModule {

  @Singleton
  @Provides
  fun provideBirthdayDatabase(application: Context): BirthdayDatabase {
    return Room.databaseBuilder(application, BirthdayDatabase::class.java, "database").fallbackToDestructiveMigration().build()
  }

  @Singleton
  @Provides
  fun providesBirthdayDao(database: BirthdayDatabase)=database.birthdayDao()


  @Singleton
  @Provides
  fun providesBirthdayListManager(dao: BirthdayDao): BirthdayListManager{
    return BirthdayListManager(dao)
  }

  @Singleton
  @Provides
  fun provideBirthdayRepository(manager: BirthdayListManager): DatabaseRepository{
    return BirthdayRepository(manager)
  }

  @Singleton
  @Provides
  fun providesBirthdayInteractor(repository: BirthdayRepository): BirthdayInteractor {
    return BirthdayInteractor(repository)
  }
}
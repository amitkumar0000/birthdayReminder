package com.birthday.ui.birthdayFeed

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.birthday.common.DateConverter

@Database(entities = [BirthdayList::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class BirthdayDatabase : RoomDatabase() {
  abstract fun birthdayDao(): BirthdayDao
}
package com.birthday.ui.birthdayFeed

import io.reactivex.Single
import java.util.Date

open class BirthdayListManager(val database: BirthdayDatabase) {

  open fun loadAllItem(): Single<List<BirthdayList>> {
    return database.birthdayDao().getAll().toSingle()
  }

  open fun insertInfo(item: BirthdayList): Long {
    return database.birthdayDao().insert(item)
  }

  open fun deleteItem(id: Long): Int {
    return database.birthdayDao().delete(id)

  }

  open fun updateItem(imagePath:String,name: String, dob: Date) : Int {
    return database.birthdayDao().update(imagePath,name,dob)
  }

  open fun updateDateItem(date: Date,name:String,dob: Date):Int {
    return database.birthdayDao().updateDate(date,name,dob)

  }

  open fun updateTimeItem(time: String,name:String,dob: Date): Int {
    return database.birthdayDao().updateTime(time,name,dob)

  }
}
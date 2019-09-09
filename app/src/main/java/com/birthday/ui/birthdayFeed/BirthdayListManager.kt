package com.birthday.ui.birthdayFeed

import io.reactivex.Single
import java.util.Date

class BirthdayListManager(private val database: BirthdayDatabase) {

  fun loadAllItem(): Single<List<BirthdayList>> {
    return database.birthdayDao().getAll().toSingle()
  }

  fun insertInfo(item: BirthdayList): Long {
    return database.birthdayDao().insert(item)
  }

  fun deleteItem(id: Long): Int {
    return database.birthdayDao().delete(id)

  }

  fun updateItem(imagePath:String,name: String, dob: Date) : Int {
    return database.birthdayDao().update(imagePath,name,dob)
  }
}
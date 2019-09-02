package com.birthday.ui.birthdayFeed

import io.reactivex.Single

class BirthdayListManager(private val database: BirthdayDatabase) {

  fun loadAllItem(): Single<List<BirthdayList>> {
    return database.birthdayDao().getAll().toSingle()
  }

  fun insertInfo(item: BirthdayList): Long {
    return database.birthdayDao().insert(item)
  }
}
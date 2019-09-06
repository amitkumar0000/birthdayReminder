package com.birthday.ui.birthdayFeed

import io.reactivex.Completable
import io.reactivex.Single

interface  DatabaseRepository {
  fun getAllItem(): Single<List<BirthdayList>>
  fun insertItem(item:BirthdayList): Completable
  fun deleteItem(id:Long): Completable
}
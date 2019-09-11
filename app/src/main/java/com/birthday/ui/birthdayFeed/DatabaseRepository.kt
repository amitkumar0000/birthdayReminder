package com.birthday.ui.birthdayFeed

import io.reactivex.Completable
import io.reactivex.Single
import java.util.Date

interface  DatabaseRepository {
  fun getAllItem(): Single<List<BirthdayList>>
  fun insertItem(item:BirthdayList): Completable
  fun deleteItem(id:Long): Completable
  fun updateItem(path:String,name:String,dob:Date): Completable
  fun updateDateItem(date:Date,name: String,dob: Date): Completable
  fun updateTimeItem(time:String,name: String,dob: Date): Completable
}
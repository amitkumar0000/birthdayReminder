package com.birthday.ui.birthdayFeed

import io.reactivex.Completable
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

open class BirthdayInteractor @Inject constructor(
  val birthdayRepository: BirthdayRepository
) {

  open fun feedContent(): Single<List<BirthdayList>> {
    return birthdayRepository.getAllItem()
  }

  open fun saveContent(info: BirthdayList) : Completable{
     return birthdayRepository.insertItem(info)
  }

  open fun deleteId(id: Long): Completable {
    return birthdayRepository.deleteItem(id)
  }

  open fun updateContent(imagePath:String,name: String, dob: Date): Completable {
    return birthdayRepository.updateItem(imagePath,name,dob)

  }

  open fun updateRemainderTimeContent(date: Date,name:String,dob: Date): Completable {
    return birthdayRepository.updateDateItem(date,name,dob)

  }

  open fun updateNotificationTimeContent(time: String,name:String,dob: Date): Completable {
    return birthdayRepository.updateTimeItem(time,name,dob)

  }
}
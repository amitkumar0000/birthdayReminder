package com.birthday.ui.birthdayFeed

import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class BirthdayInteractor @Inject constructor(
  val birthdayRepository: BirthdayRepository
) {

  fun feedContent(): Single<List<BirthdayList>> {
    return birthdayRepository.getAllItem()
  }

  fun saveContent(info: BirthdayList) : Completable{
     return birthdayRepository.insertItem(info)
  }
}
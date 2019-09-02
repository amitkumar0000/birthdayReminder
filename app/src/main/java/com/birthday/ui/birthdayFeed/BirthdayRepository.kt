package com.birthday.ui.birthdayFeed

import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class BirthdayRepository @Inject constructor(val manager: BirthdayListManager) : DatabaseRepository {

  override fun getAllItem(): Single<List<BirthdayList>> {
    return manager.loadAllItem()
  }

  override fun insertItem(item: BirthdayList): Completable {
    return Completable.fromAction {
      manager.insertInfo(item)
    }
  }

  override fun deleteItem(): Completable {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}
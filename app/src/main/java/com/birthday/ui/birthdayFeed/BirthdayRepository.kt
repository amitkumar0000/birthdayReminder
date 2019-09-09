package com.birthday.ui.birthdayFeed

import io.reactivex.Completable
import io.reactivex.Single
import java.util.Date
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

  override fun deleteItem(id:Long): Completable {
    return Completable.fromAction {
      manager.deleteItem(id)
    }
  }

  fun updateItem(imagePath:String,name: String, dob: Date): Completable {
    return Completable.fromAction {
      manager.updateItem(imagePath,name,dob)
    }
  }
}
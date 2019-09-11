package com.birthday.ui.birthdayFeed

import io.reactivex.Single
import java.util.Date

open class BirthdayListManager(val dao: BirthdayDao) {

  open fun loadAllItem(): Single<List<BirthdayList>> {
    return dao.getAll().toSingle()
  }

  open fun insertInfo(item: BirthdayList): Long {
    return dao.insert(item)
  }

  open fun deleteItem(id: Long): Int {
    return dao.delete(id)

  }

  open fun updateItem(imagePath:String,name: String, dob: Date) : Int {
    return dao.update(imagePath,name,dob)
  }

  open fun updateDateItem(date: Date,name:String,dob: Date):Int {
    return dao.updateDate(date,name,dob)

  }

  open fun updateTimeItem(time: String,name:String,dob: Date): Int {
    return dao.updateTime(time,name,dob)
  }
}
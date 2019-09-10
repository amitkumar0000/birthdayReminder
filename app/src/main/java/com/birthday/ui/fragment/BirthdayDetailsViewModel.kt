package com.birthday.ui.fragment

import androidx.lifecycle.ViewModel
import com.birthday.ui.birthdayFeed.BirthdayDBUpdate
import com.birthday.ui.birthdayFeed.BirthdayInteractor
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.Date

class BirthdayDetailsViewModel(val birthdayInteractor: BirthdayInteractor) : ViewModel() {

  private val state by lazy { BehaviorSubject.create<BirthdayDBUpdate>() }

  internal fun state(): Observable<BirthdayDBUpdate> {
    return state.hide()
  }

  internal fun updateContent(imagePath:String,name: String,dob: Date) {
    birthdayInteractor.updateContent(imagePath,name,dob)
      .subscribeOn(Schedulers.computation())
      .subscribe {
        Timber.d("Update $imagePath with name $name and dob $dob is success")
      }
  }

  internal fun updateRemainderTimeContent(date:Date,name:String,dob: Date){
    birthdayInteractor.updateRemainderTimeContent(date,name,dob)
      .subscribeOn(Schedulers.computation())
      .subscribe {
        Timber.d("updateRemainderTimeContent with $date is success")
      }

  }

  internal fun updateNotificationTimeContent(time: String,name:String,dob: Date) {
    birthdayInteractor.uodateNotificationTimeContent(time,name,dob)
      .subscribeOn(Schedulers.computation())
      .subscribe {
        Timber.d("Update updateNotificationTimeContent $time is success")
      }
  }

}
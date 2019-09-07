package com.birthday.ui.birthdayFeed

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class BithdayAddViewModel(val birthdayInteractor: BirthdayInteractor) : ViewModel() {

  private val state by lazy { BehaviorSubject.create<BirthdayDBUpdate>() }

  internal fun state(): Observable<BirthdayDBUpdate> {
    return state.hide()
  }
  internal fun saveContent(list: BirthdayList) {
    birthdayInteractor.saveContent(list)
      .subscribeOn(Schedulers.computation())
      .subscribe {
      state.onNext(BirthdayDBUpdate.InsertSuccess(list))
    }
  }
}
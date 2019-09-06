package com.birthday.ui.birthdayFeed

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class BirthdayViewModel(val birthdayInteractor: BirthdayInteractor) : ViewModel() {

  private val state by lazy { BehaviorSubject.create<BirthdayListUpdate>() }

  internal fun state(): Observable<BirthdayListUpdate> {
    return state.hide()
  }

  internal fun loadContent() {
    state.onNext(BirthdayListUpdate.Loading)
    birthdayInteractor.feedContent()
      .subscribeOn(Schedulers.computation())
      .subscribe({
        if (it.isNotEmpty())
          state.onNext(BirthdayListUpdate.Content(it))
        else
          state.onNext(BirthdayListUpdate.Error)
      }, {
        state.onNext(BirthdayListUpdate.Error)
      })
  }

  internal fun saveContent(list: BirthdayList) {
    birthdayInteractor.saveContent(list)
      .subscribeOn(Schedulers.computation())
      .subscribe {
        state.onNext(BirthdayListUpdate.InsertSuccess)
      }
  }

  fun deleteContent(id: Long) {
    birthdayInteractor.deleteId(id)
      .subscribeOn(Schedulers.io())
      .subscribe{
        state.onNext(BirthdayListUpdate.deleteSuccess)
      }
  }
}
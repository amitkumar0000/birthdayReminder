package com.birthday.ui.birthdayFeed

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import javax.inject.Inject

class BirthdayAddViewModelFactory @Inject constructor(birthdayInteractor: BirthdayInteractor) {

  private val factory = object : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return BithdayAddViewModel(birthdayInteractor) as T
    }
  }

  fun getInstance(fragment: Fragment): BithdayAddViewModel {
    return ViewModelProviders.of(fragment,factory).get(BithdayAddViewModel::class.java)
  }
}
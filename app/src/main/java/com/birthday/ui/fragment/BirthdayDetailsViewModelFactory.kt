package com.birthday.ui.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.birthday.ui.birthdayFeed.BirthdayInteractor
import javax.inject.Inject

class BirthdayDetailsViewModelFactory @Inject constructor(birthdayInteractor: BirthdayInteractor){

  private val factory = object : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return BirthdayDetailsViewModel(birthdayInteractor) as T
    }
  }

  fun getInstance(fragment: Fragment): BirthdayDetailsViewModel {
    return ViewModelProviders.of(fragment,factory).get(BirthdayDetailsViewModel::class.java)
  }

}
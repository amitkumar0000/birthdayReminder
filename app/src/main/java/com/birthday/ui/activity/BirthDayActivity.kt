package com.birthday.ui.activity

import android.os.Bundle
import com.birthday.ui.birthdayFeed.BirthdayFeedFragment

class BirthDayActivity() : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    navigationFragmentManager.addAsBaseFragment(BirthdayFeedFragment.newInstance())
  }


}

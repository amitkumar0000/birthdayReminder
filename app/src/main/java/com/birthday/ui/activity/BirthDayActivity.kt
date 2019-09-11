package com.birthday.ui.activity

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import com.birthday.common.Utils
import com.birthday.ui.birthdayFeed.BirthdayFeedFragment
import com.birthday.ui.fragment.BirthdayDetailsFragment
import java.text.SimpleDateFormat
import java.util.Date

class BirthDayActivity() : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setFragment(BirthdayFeedFragment.newInstance())
  }

  fun setFragment(fragment: BirthdayFeedFragment){
    navFragmentManager.addAsBaseFragment(fragment)
  }

  @VisibleForTesting
  fun setupDetailFragment(
    imagePath: String,
    name: String, details: String,
    dob: Date,
    remainderDate: Date,
    remainderTime: String

  ) {
    val fragment = BirthdayDetailsFragment.newInstance()
    var bundle = Bundle()
    bundle.putString(Utils.IMAGE_PATH, imagePath)
    bundle.putString(Utils.NAME, name)
    bundle.putString(Utils.DOB, SimpleDateFormat("dd MMMM yyyy").format(dob))
    bundle.putString(Utils.REMAINDER_DATE, SimpleDateFormat("dd/MM/yyyy").format(remainderDate))
    bundle.putString(Utils.REMAINDER_TIME, remainderTime)
    fragment.arguments = bundle
    navFragmentManager.let {
      it.safeAddBackStack(fragment)
    }
  }


}

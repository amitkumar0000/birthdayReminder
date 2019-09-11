package com.birthday

import com.birthday.ui.birthdayFeed.BirthdayList
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TestUtils {
  fun getBirthdayList(): BirthdayList {
    return BirthdayList(
      name = "Android Testing",
      dob = getDate(),
      imagePath = "/dev/adb/image111/png",
      remainderTime = "10:00 PM",
      remainderDate = getDate()
    )
  }

  fun  getDate(): Date {
    return SimpleDateFormat("MM/DD/YYYY",
      Locale.ENGLISH).parse("09/19/1985")
  }
}
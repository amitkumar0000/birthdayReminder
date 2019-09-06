package com.birthday.ui.birthdayFeed

import java.util.Date
import kotlin.reflect.KFunction0

data class BirthdayInfoModel(
  val imagePath: String,
  val profileName: String,
  val profileDetail: String,
  val remainingDate: String,
  val dob: Date,
  val launchFunction: (String,String,String,Date) -> Unit
)
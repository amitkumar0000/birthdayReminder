package com.birthday.ui.birthdayFeed

import java.util.Date

data class BirthdayInfoModel(
  val id:Long,
  val imagePath: String,
  val profileName: String,
  val profileDetail: String,
  val remainingDate: String,
  val dob: Date,
  val launchFunction: (String,String,String,Date) -> Unit
)
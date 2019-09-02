package com.birthday.ui.birthdayFeed

import kotlin.reflect.KFunction0

data class BirthdayInfoModel(
  val imagePath: String,
  val profileName: String,
  val profileDetail: String,
  val remainingDate: String,
  val launchFunction: (String,String,String) -> Unit
)
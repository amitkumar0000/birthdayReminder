package com.birthday.ui.birthdayFeed

import java.util.Date
import kotlin.reflect.KFunction6

data class BirthdayInfoModel(
  val id:Long,
  val imagePath: String,
  val profileName: String,
  val profileDetail: String,
  val remainingDate: String,
  var remainderDate:Date,
  var remainderTime: String,
  val dob: Date,
  val launchFunction: KFunction6<@ParameterName(name = "imagePath") String, @ParameterName(
    name = "name"
  ) String, @ParameterName(name = "details") String, @ParameterName(name = "dob") Date, @ParameterName(
    name = "remainderDate"
  ) Date, @ParameterName(name = "remainderTime") String, Unit>
)
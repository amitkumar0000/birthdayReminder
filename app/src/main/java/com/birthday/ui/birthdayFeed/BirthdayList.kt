package com.birthday.ui.birthdayFeed

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class BirthdayList(
  var name:String,
  var dob: Date,
  var imagePath:String,
  var remainderDate:Date,
  var remainderTime: String
){
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0
}
package com.birthday.common

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateConverter {

  @TypeConverter
  @JvmStatic
  fun fromDate(dob:String) : Date{
    return  SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
      Locale.ENGLISH).parse(dob)
  }

  @TypeConverter
  @JvmStatic
  fun toDate(dob:Date) : String{
      return dob.toString()
  }

}
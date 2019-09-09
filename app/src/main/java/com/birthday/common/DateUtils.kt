package com.birthday.common

import java.util.Date

object DateUtils {
  fun getRemainingDays(dob: Date, cDate: Date):Int{

    var dAbsDays=absoluteDay(dob.month, dob.date)

    if(cDate.month > dob.month || (dob.month == cDate.month && cDate.date>dob.date)){
       dAbsDays += 365
    }
    
    var cDays = absoluteDay(cDate.month,cDate.date)

    return Math.abs(dAbsDays - cDays)
  }

  private fun isLeapYear(year: Int): Boolean {
    return (year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0))
  }

  fun absoluteDay(month: Int, day: Int): Int {
    if (month == 1)
      return day
    if (month == 2)
      return day + 31
    if (month == 3)
      return day + 60 
    if (month == 4)
      return day + 91 
    if (month == 5)
      return day + 121 
    if (month == 6)
      return day + 152 
    if (month == 7)
      return day + 182 
    if (month == 8)
      return day + 213 
    if (month == 9)
      return day + 244 
    if (month == 10)
      return day + 274 
    if (month == 11)
      return day + 305 
    return if (month == 12)
      day + 335 
    else
      0
  }
}
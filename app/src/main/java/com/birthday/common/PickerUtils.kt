package com.birthday.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.sql.Time
import java.text.Format
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.reflect.KFunction1

object PickerUtils {

  fun showDatePicker(
    context: Context,
    kFunction1: KFunction1<@ParameterName(name = "text") String, Unit>
  ) {
    val mcurrentDate = Calendar.getInstance()
    val mYear = mcurrentDate.get(Calendar.YEAR)
    val mMonth = mcurrentDate.get(Calendar.MONTH)
    val mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH)

    val mDatePicker: DatePickerDialog
    mDatePicker =
      DatePickerDialog(context,
        DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
          var selectedmonth = selectedmonth
          // TODO Auto-generated method stub
          /*      Your code   to get date and time    */
          selectedmonth += 1
          var selectedDay:String = "$selectedday"
          if(selectedday<10){
            selectedDay= "0$selectedday"
          }
          var selectedMonth:String = "$selectedmonth"
          if(selectedmonth<10){
            selectedMonth= "0$selectedmonth"
          }
          kFunction1("$selectedDay/$selectedMonth/$selectedyear")
        }, mYear, mMonth, mDay)
    mDatePicker.setTitle("Select Date")
    mDatePicker.show()  }

  fun showTimePicker(
    context: Context,
    kFunction1: KFunction1<@ParameterName(name = "text") String, Unit>
  ){
    val mcurrentTime = Calendar.getInstance()
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)
    val mTimePicker: TimePickerDialog
    mTimePicker = TimePickerDialog(context,
      TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
        kFunction1 (getTime(selectedHour,selectedMinute))
      }, hour, minute, false
    )
    mTimePicker.setTitle("Select Time")
    mTimePicker.show()
  }

  private fun getTime(hr: Int, min: Int): String {
    val tme = Time(hr, min, 0)//seconds by default set to zero
    val formatter: Format
    formatter = SimpleDateFormat("h:mm a")
    return formatter.format(tme)
  }
}
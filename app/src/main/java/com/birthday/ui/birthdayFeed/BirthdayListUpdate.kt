package com.birthday.ui.birthdayFeed

sealed class BirthdayListUpdate {
  object Loading : BirthdayListUpdate()
  object Error : BirthdayListUpdate()
  data class InsertSuccess(val item:BirthdayList) : BirthdayListUpdate()
  object deleteSuccess : BirthdayListUpdate()
  data class Content(val list: List<BirthdayList>) : BirthdayListUpdate()
}

sealed class BirthdayDBUpdate{
  data class InsertSuccess(val item:BirthdayList) : BirthdayDBUpdate()
}
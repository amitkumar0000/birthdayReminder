package com.birthday.ui.birthdayFeed

sealed class BirthdayListUpdate {
  object Loading : BirthdayListUpdate()
  object Error : BirthdayListUpdate()
  data class Content(val list: List<BirthdayList>) : BirthdayListUpdate()
}

sealed class BirthdayDBUpdate{
  object InsertSuccess : BirthdayDBUpdate()
}
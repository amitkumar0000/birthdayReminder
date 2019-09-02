package com.birthday.ui.birthdayFeed

import com.airbnb.epoxy.TypedEpoxyController

class BirthdayController : TypedEpoxyController<List<BirthdayInfoModel>>() {
  override fun buildModels(data: List<BirthdayInfoModel>?) {
    data?.forEach {
      birthdayView {
        id(it.hashCode())
        imagepath(it.imagePath)
        profileName(it.profileName)
        profileDetails(it.profileDetail)
        remainingDate(it.remainingDate)
        launchBDP(it.launchFunction)
      }
    }
  }
}


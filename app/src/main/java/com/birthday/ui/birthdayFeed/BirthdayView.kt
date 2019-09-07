package com.birthday.ui.birthdayFeed

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.birthday.ui.R
import com.bumptech.glide.Glide
import java.util.Date

@EpoxyModelClass(layout = R.layout.birthday_info_row)
abstract class BirthdayView : EpoxyModelWithHolder<BirthdayViewHolder>() {

  @EpoxyAttribute
  lateinit var imagepath: String
  @EpoxyAttribute
  lateinit var profileName: String
  @EpoxyAttribute
  lateinit var profileDetails: String
  @EpoxyAttribute
  lateinit var remainingDate: String
  @EpoxyAttribute
  lateinit var dob: Date
  @EpoxyAttribute
  lateinit var launchBDP: (String,String,String,Date)->Unit


  override fun bind(holder: BirthdayViewHolder?) {
    super.bind(holder)
    holder?.rowContainer?.setOnClickListener{
             launchBDP(imagepath,profileName,profileDetails,dob)
    }
    holder?.profileImage?.let {
      Glide.with(it.context).load(imagepath).into(it)
    }
    holder?.profileName?.text = profileName
    holder?.profiledetail?.text = profileDetails
    holder?.remainingDate?.text = remainingDate
  }
}

class BirthdayViewHolder : EpoxyHolder() {

  lateinit var profileImage: ImageView
  lateinit var profileName: TextView
  lateinit var profiledetail: TextView
  lateinit var remainingDate: TextView
  lateinit var imageView: ImageView
  lateinit var rowContainer: ConstraintLayout

  override fun bindView(itemView: View) {
    profileImage = itemView.findViewById(R.id.profile_image)
    profileName = itemView.findViewById(R.id.profile_name)
    profiledetail = itemView.findViewById(R.id.profile_birth_details)
    remainingDate = itemView.findViewById(R.id.remaining_date)
    imageView = itemView.findViewById(R.id.imageView)
    rowContainer = itemView.findViewById(R.id.row_container)
  }
}
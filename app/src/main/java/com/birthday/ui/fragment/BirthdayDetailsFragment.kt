package com.birthday.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.get
import com.birthday.common.Utility
import com.birthday.ui.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.birthday_profile_layout.bdpToolbar
import kotlinx.android.synthetic.main.birthday_profile_layout.profileDob
import kotlinx.android.synthetic.main.birthday_profile_layout.profileImage
import kotlinx.android.synthetic.main.birthday_profile_layout.profileName
import kotlinx.android.synthetic.main.birthday_share_layout.sharelayout
import android.content.pm.ResolveInfo

class BirthdayDetailsFragment : BaseNavigationFragment() {

  val backButton by lazy { resources.getDrawable(R.drawable.back, null) }

  private val call by lazy { resources.getDrawable(R.drawable.call, null) }
  private val text by lazy { resources.getDrawable(R.drawable.text, null) }
  private val email by lazy { resources.getDrawable(R.drawable.mail, null) }
  private val share by lazy { resources.getDrawable(R.drawable.share, null) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_birthday_details, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setToolbar()
    val bundle = arguments



    bundle?.let {
      Glide.with(requireContext()).load(bundle.getString(Utility.IMAGE_PATH)).into(profileImage)
      profileName.text = bundle.getString(Utility.NAME)
      profileDob.text = bundle.getString(Utility.DOB)
    }

    setShareView()
  }

  fun setShareView() {
    sharelayout.get(0).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.text)
      findViewById<CircleImageView>(R.id.share_image_container).apply {
        setImageDrawable(text)
        setBackgroundColor(getColor(requireContext(), android.R.color.holo_green_light))
      }
      setOnClickListener {
        startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MESSAGING))
      }
    }

    sharelayout.get(1).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.call)
      findViewById<CircleImageView>(R.id.share_image_container).apply {
        setBackgroundColor(getColor(requireContext(), R.color.blue_light))
        setImageDrawable(call)
      }
      setOnClickListener {
        startActivity(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI))
      }
    }

    sharelayout.get(2).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.email)
      findViewById<CircleImageView>(R.id.share_image_container).apply {
        setImageDrawable(email)
        setBackgroundColor(getColor(requireContext(), android.R.color.holo_red_dark))
        alpha = 0.6F
      }
      setOnClickListener {
        openGmail(arrayOf("amit.ap1924@gmail.com"), "Happy birthday", "")
      }
    }

    sharelayout.get(3).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.share)
      findViewById<CircleImageView>(R.id.share_image_container).apply {
        setImageDrawable(share)
        setBackgroundColor(getColor(requireContext(), android.R.color.holo_blue_dark))

      }
      setOnClickListener {

        val sendIntent: Intent = Intent().apply {
          action = Intent.ACTION_SEND
          putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
          type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
      }
    }
  }

  private fun openGmail(email: Array<String>, subject: String, content: String) {
    val emailIntent = Intent(Intent.ACTION_SEND)
    emailIntent.putExtra(Intent.EXTRA_EMAIL, email)
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    emailIntent.type = "text/plain"
    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content)
    val pm = activity?.packageManager
    val matches = pm?.queryIntentActivities(emailIntent, 0)
    var best: ResolveInfo? = null
    matches?.let {
      for (info in matches)
        if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
          best = info
    }
    emailIntent.setClassName(best?.activityInfo?.packageName, best?.activityInfo?.name)

    startActivity(emailIntent)
  }

  private fun setToolbar() {
    bdpToolbar.apply {
      navigationIcon = backButton
      title = getString(R.string.birthdayTitle)
      setTitleTextColor(getColor(requireContext(), R.color.white))
      setNavigationOnClickListener {
        fragmentManager?.popBackStack()
      }
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() =
      BirthdayDetailsFragment()
  }
}

package com.birthday.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.get
import com.birthday.common.PermissionUtility
import com.birthday.ui.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.birthday_profile_layout.bdpToolbar
import kotlinx.android.synthetic.main.birthday_profile_layout.profileDob
import kotlinx.android.synthetic.main.birthday_profile_layout.profileImage
import kotlinx.android.synthetic.main.birthday_profile_layout.profileName
import kotlinx.android.synthetic.main.birthday_share_layout.sharelayout
import android.content.pm.ResolveInfo
import kotlinx.android.synthetic.main.remainder_layout.notificationtime
import kotlinx.android.synthetic.main.remainder_layout.remainderTime
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import com.birthday.common.ImageStorageManager
import com.birthday.common.ImageUtils
import com.birthday.common.PickerUtils.showDatePicker
import com.birthday.common.PickerUtils.showTimePicker
import com.birthday.common.REQUEST_CAMERA
import com.birthday.common.SELECT_FILE
import com.birthday.scheduler.BirthdayWorkManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.birthday_add_fragment.nameInput
import kotlinx.android.synthetic.main.birthday_add_fragment.profile_image
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

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
      Glide.with(requireContext()).load(bundle.getString(PermissionUtility.IMAGE_PATH)).into(profileImage)
      profileName.text = bundle.getString(PermissionUtility.NAME)
      profileDob.text = bundle.getString(PermissionUtility.DOB)
    }

    notificationtime.setOnClickListener {
      showTimePicker(requireContext(), ::timePickerText)
    }

    remainderTime.setOnClickListener {
      showDatePicker(requireContext(), ::DatePickerText)
    }

    profileImage.setOnClickListener{
      ImageUtils.selectImage(fragment = this@BirthdayDetailsFragment,context = requireContext())
    }

    setShareView()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == SELECT_FILE)
        onSelectFromGalleryResult(data)
      else if (requestCode == REQUEST_CAMERA)
        onCaptureImageResult(data)
    }
  }

  private fun onSelectFromGalleryResult(data: Intent?) {
    var bm: Bitmap? = null
    if (data != null) {
      try {
        val uri = data.data
        bm = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        saveImageToInternalStorage(bm, profileName.text.toString() + System.currentTimeMillis())
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  private fun saveImageToInternalStorage(bm: Bitmap, fileName: String) {
    ImageStorageManager.saveToInternalStorage(requireContext(), bm, fileName)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        Glide.with(requireContext()).load(it).into(profileImage)
      }, {
        Log.d("TAG", it.toString())
      })
  }

  private fun onCaptureImageResult(data: Intent?) {
    val thumbnail = data?.extras?.get("data") as Bitmap?
    thumbnail?.let {
      saveImageToInternalStorage(thumbnail, profileName.text.toString() + System.currentTimeMillis())
    }
  }



  private fun timePickerText(text: String) {
    notificationtime.text = text
    reschuldeAlarm()
  }

  private fun DatePickerText(text: String) {
    remainderTime.text = text
    reschuldeAlarm()
  }

  private fun reschuldeAlarm(){
    BirthdayWorkManager().startOneTimeWork(profileName.text.toString(), SimpleDateFormat("dd MMM yyyy",
      Locale.ENGLISH).parse(profileDob.text.toString()).time)
      .delay(1,TimeUnit.MINUTES)
      .subscribeOn(Schedulers.io())
      .subscribe()
  }

  private fun setShareView() {
    sharelayout.get(0).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.text)
      findViewById<FloatingActionButton>(R.id.fab).apply {
        setImageDrawable(text)
        backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), R.color.blue_light))
      }
      setOnClickListener {
        startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MESSAGING))
      }
    }

    sharelayout.get(1).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.call)
      findViewById<FloatingActionButton>(R.id.fab).apply {
        backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), android.R.color.holo_green_light))
        setImageDrawable(call)
      }
      setOnClickListener {
        startActivity(Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI))
      }
    }

    sharelayout.get(2).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.email)
      findViewById<FloatingActionButton>(R.id.fab).apply {
        setImageDrawable(email)
        backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), android.R.color.holo_red_dark))
        alpha = 0.6F
      }
      setOnClickListener {
        openGmail(arrayOf("amit.ap1924@gmail.com"), "Happy birthday", "")
      }
    }

    sharelayout.get(3).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.share)
      findViewById<FloatingActionButton>(R.id.fab).apply {
        setImageDrawable(share)
        backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), android.R.color.holo_blue_dark))
      }
      setOnClickListener {

        val sendIntent: Intent = Intent().apply {
          action = Intent.ACTION_SEND
          putExtra(Intent.EXTRA_TEXT, getString(R.string.happybday, profileName.text.toString()))
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

package com.birthday.ui.birthdayFeed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.birthday.BirthdayApplication
import com.birthday.ui.R
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.birthdayfeed_fragment.birthday_container as infoContainer
import kotlinx.android.synthetic.main.birthdayfeed_fragment.birthday_info_list as birthdayList
import kotlinx.android.synthetic.main.birthdayfeed_fragment.toolbar
import java.util.Calendar
import javax.inject.Inject
import android.text.format.DateFormat

private const val PAGE_LOADING_STATE = 0
private const val PAGE_ERROR_STATE = 1
private const val PAGE_CONTENT_STATE = 2
private const val DIALOG_PICKER = 100

class BirthdayFeedFragment : Fragment() {

  private val settingDrawable by lazy { resources.getDrawable(R.drawable.ic_settings_black_24dp, null) }

  private val birthdayController by lazy { BirthdayController() }

  @Inject
  lateinit var viewModelFactory: BirthdayViewModelFactory

  private val viewModel by lazy { viewModelFactory.getInstance(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    BirthdayApplication.component.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.birthdayfeed_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupToolbar()

    birthdayList.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = birthdayController.adapter
      addItemDecoration(ItemDivider(requireContext()))
    }

    viewModel.state()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        when (it) {
          is BirthdayListUpdate.Loading -> {
            infoContainer.displayedChild = PAGE_LOADING_STATE
          }
          is BirthdayListUpdate.Error -> {
            infoContainer.displayedChild = PAGE_ERROR_STATE
          }
          is BirthdayListUpdate.Content -> {
            val bInfoModelList = ArrayList<BirthdayInfoModel>()
            it.list?.forEach {
              bInfoModelList.add(transform(it))
            }
            birthdayController.setData(bInfoModelList)
            infoContainer.displayedChild = PAGE_CONTENT_STATE
          }
        }
      }

    requestContent()
  }

  private fun transform(it: BirthdayList): BirthdayInfoModel {
    val imagePath = it.imagePath
    val profileName = it.name
    val dob = it.dob

    val difference = Calendar.getInstance().time.time - dob.time
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val yearInMilli = daysInMilli * 365
    val remainingDays = (difference / daysInMilli) / 1000  //TODO fix it
    val profileDetail: String = getString(
      R.string.birthdayDetail, difference / yearInMilli,
      DateFormat.format("dd", dob) as String,
      DateFormat.format("MMM", dob) as String
    )
    val remainingDay = getString(R.string.remainingday, remainingDays)
    return BirthdayInfoModel(imagePath, profileName, profileDetail, remainingDay)
  }

  private fun requestContent() {
    viewModel.loadContent()
  }

  private fun setupToolbar() {
    toolbar.apply {
      navigationIcon = settingDrawable
      setNavigationOnClickListener {
        Log.d("TAG", " Setting clikced")
      }
      menu.clear()
      inflateMenu(R.menu.birthday_menu)

      setOnMenuItemClickListener { item ->
        onOptionsItemSelected(item)
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
      DIALOG_PICKER -> {
        requestContent()
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.add -> {
        val birthdayAddFragment = BirthdayAddFragment()
        val ft = fragmentManager?.beginTransaction()
        ft?.addToBackStack(null)
        birthdayAddFragment.setTargetFragment(this, DIALOG_PICKER);
        birthdayAddFragment.show(ft, null)
        Log.d("TAG", " Add menu Item clikced")
        return true
      }
      else ->
        super.onOptionsItemSelected(item)
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() =
      BirthdayFeedFragment()
  }
}

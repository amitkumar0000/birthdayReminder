package com.birthday.ui.birthdayFeed

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
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
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.birthday.common.DateUtils
import com.birthday.common.PermissionUtility
import com.birthday.common.ui.ItemDivider
import com.birthday.scheduler.AlarmManagerScheduler
import com.birthday.ui.fragment.BaseNavigationFragment
import com.birthday.ui.fragment.BirthdayDetailsFragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.birthdayfeed_fragment.login_button as loginButton
import java.util.Arrays
import com.facebook.AccessToken
import com.facebook.GraphRequest
import timber.log.Timber
import com.facebook.Profile
import com.facebook.internal.ImageRequest
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.birthdayfeed_fragment.searchView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Named

private const val PAGE_LOADING_STATE = 0
private const val PAGE_ERROR_STATE = 1
private const val PAGE_CONTENT_STATE = 2
private const val DIALOG_PICKER = 100
private const val EMAIL = "email"

class BirthdayFeedFragment : BaseNavigationFragment() {

  private val settingDrawable by lazy { resources.getDrawable(R.drawable.ic_settings_black_24dp, null) }

  private val birthdayController by lazy { BirthdayController() }

  @Inject
  lateinit var viewModelFactory: BirthdayViewModelFactory

  private val viewModel by lazy { viewModelFactory.getInstance(this) }

  private val disposable by lazy { CompositeDisposable() }

  private val callbackManager by lazy { CallbackManager.Factory.create() }

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

  var backData:List<BirthdayInfoModel>? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    setupFBlogin()

    setupToolbar()

    birthdayList.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = birthdayController.adapter
      addItemDecoration(ItemDivider(requireContext()))
    }

    setupSwipeToDelete()

    disposable.add(
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
              backData = bInfoModelList
              infoContainer.displayedChild = PAGE_CONTENT_STATE
            }
            is BirthdayListUpdate.InsertSuccess -> {
              requestContent()
            }
            is BirthdayListUpdate.deleteSuccess -> {
              requestContent()
            }
          }
        }
    )

    setSearchView()

    requestContent()
  }

  private fun setSearchView() {

    disposable.add(fromview(searchView).debounce(300, TimeUnit.MILLISECONDS)
      .filter { text -> !text.isEmpty() && text.length >= 3 }
      .map { text -> text.toLowerCase().trim() }
      .distinctUntilChanged()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe{text->
        var filterdata = arrayListOf<BirthdayInfoModel>()
        backData?.forEach {
          if(it.profileName.contains(text,true))
            filterdata.add(it)
        }
        if(filterdata.size>0)
          birthdayController.setData(filterdata)
      }
    )

    searchView.setOnCloseListener {
      birthdayController.setData(backData)
      subject.onNext("dummy")
      true
    }
  }

  var subject = PublishSubject.create<String>();


  private fun fromview(searchView: SearchView): Observable<String> {

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        searchView.clearFocus()
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        subject.onNext(newText)
        return false
      }
    })
    return subject;
  }

  private fun setupSwipeToDelete() {
    var itemTouchHelperCallback =
      object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
          recyclerView: RecyclerView,
          viewHolder: RecyclerView.ViewHolder,
          target: RecyclerView.ViewHolder
        ): Boolean {
          Timber.d("Item onMove")
          return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
          val content = birthdayController.currentData?.get(viewHolder.adapterPosition)

          Timber.d("Item Swiped ")
          content?.let { viewModel.deleteContent(it.id) }
        }
      }
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(birthdayList)
  }

  private fun setupFBlogin() {
    loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"))

    loginButton.fragment = this

    loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
      override fun onSuccess(result: LoginResult?) {
        Timber.d("FB Login Success")
        result?.let {
          getFacebookContent(it)
        }
      }

      override fun onCancel() {
        Timber.d("FB Login onCancel")
      }

      override fun onError(error: FacebookException?) {
        Timber.d("FB Login onError")
      }
    })
  }

  private fun getFacebookContent(loginResult: LoginResult) {
    val request = GraphRequest.newMeRequest(
      loginResult.accessToken
    ) { `jsonObject`,
      response ->
      run {

        Timber.d(" Response $jsonObject  $response")
        viewModel.saveContent(
          BirthdayList(
            name = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"),
            dob = Date(jsonObject.getString("birthday")),
            imagePath = ImageRequest.getProfilePictureUri(Profile.getCurrentProfile().id, 100, 100).toString()
          )
        )
      }
    }
    val parameters = Bundle();
    parameters.putString("fields", "id,email,first_name,last_name,birthday,friends");
    request.parameters = parameters;
    request.executeAsync();
  }

  private fun transform(it: BirthdayList): BirthdayInfoModel {
    val imagePath = it.imagePath
    val profileName = it.name
    val dob: Date = it.dob
    val ct = Calendar.getInstance().time
    val monthRem = (ct.month - dob.month)
    val dayRem = ct.date - dob.date
    val additionalDay = additionalDay(ct.year, ct.month, dob.month)
    val totalRem = Math.abs(dayRem) + Math.abs(monthRem * 30) + additionalDay
    var age = ct.year - dob.year
    if (monthRem > 0 || (monthRem == 0 && dayRem > 0)) {
      age += 1
    }
    val profileDetail: String = getString(
      R.string.birthdayDetail, age,
      DateFormat.format("dd", dob) as String,
      DateFormat.format("MMM", dob) as String
    )
    val remainingDay = getString(R.string.remainingday, DateUtils.getRemainingDays(dob, Calendar.getInstance().time))
    return BirthdayInfoModel(
      it.id,
      imagePath,
      profileName,
      profileDetail,
      remainingDay,
      it.dob,
      ::bdpLauncher
    )
  }

  private val lookup =
    mapOf<Int, Int>(1 to 1, 2 to -1, 3 to 1, 4 to 0, 5 to 1, 6 to 0, 7 to 1, 8 to 1, 9 to 0, 10 to 1, 11 to 0, 12 to 1)

  private fun additionalDay(year: Int, cMn: Int, bMon: Int): Int {
    var aD = 0
    var cMon = cMn
    while (cMon != bMon) {
      if (cMon == 2) {
        if ((cMon > bMon && isleap(year + 1)) || isleap(year)) {
          aD -= 1
        } else {
          aD -= 2
        }
        cMon = (cMon + 1) % 12
        if (cMon == 0) cMon = 12
        continue
      }
      aD += lookup.get(cMon)!!
      cMon = (cMon + 1) % 12
      if (cMon == 0) cMon = 12
    }
    return aD
  }

  private fun isleap(year: Int): Boolean {
    // then it is a leap year
    if (year % 400 == 0 || year % 4 == 0)
      return true;

    // Else If a year is muliplt of 100,
    // then it is not a leap year
    if (year % 100 == 0)
      return false;
    return false;
  }

  private fun requestContent() {
    viewModel.loadContent()
  }

  private fun setupToolbar() {
    toolbar.apply {
      navigationIcon = settingDrawable
        setNavigationOnClickListener {
          syncFacebookContent()
        }
      menu.clear()
      inflateMenu(R.menu.birthday_menu)

      setOnMenuItemClickListener { item ->
        onOptionsItemSelected(item)
      }
    }
  }

  private fun syncFacebookContent() {
    val accessToken = AccessToken.getCurrentAccessToken()
    val isLoggedIn = accessToken != null && !accessToken.isExpired
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
      DIALOG_PICKER -> {
        requestContent()
      }
    }
  }

  private fun bdpLauncher(imagePath: String, name: String, details: String, dob: Date) {
    val fragment = BirthdayDetailsFragment.newInstance()
    var bundle = Bundle()
    bundle.putString(PermissionUtility.IMAGE_PATH, imagePath)
    bundle.putString(PermissionUtility.NAME, name)
    bundle.putString(PermissionUtility.DOB, SimpleDateFormat("dd MMMM yyyy").format(dob))
    fragment.arguments = bundle
    navigationManagerHolder.getNavigationFragmentManager().let {
      it.safeAddBackStack(fragment)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
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

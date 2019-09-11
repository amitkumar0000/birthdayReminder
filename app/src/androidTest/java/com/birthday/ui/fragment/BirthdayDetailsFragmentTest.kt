package com.birthday.ui.fragment

import android.net.Uri
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.birthday.ui.R
import com.birthday.ui.activity.BirthDayActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class BirthdayDetailsFragmentTest {
  @Rule
  @JvmField
  var activityActivityTestRule = ActivityTestRule(BirthDayActivity::class.java)

  var testContext = InstrumentationRegistry.getInstrumentation().getContext()
  var testRes = testContext.getResources()

  @Before
  fun setup() {
    activityActivityTestRule.activity.setupDetailFragment(
      getURLForResource(R.drawable.profile_icon)
      , "testName", "sdssd", getDate(), getDate(), "10:00 AM"
    )
  }

  @Test
  fun testbdpToolbar() {
    onView(withId(R.id.bdpToolbar))
      .check(matches(ViewMatchers.isDisplayed()))
  }

  fun getURLForResource(resourceId: Int) =
    Uri.parse("android.resource:// ${testContext.packageName}/resourceId)").toString()

  fun getDate(): Date {
    return SimpleDateFormat(
      "MM/DD/YYYY",
      Locale.ENGLISH
    ).parse("09/19/1985")
  }
}
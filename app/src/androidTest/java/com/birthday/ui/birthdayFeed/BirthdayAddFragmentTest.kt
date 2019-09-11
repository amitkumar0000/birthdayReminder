package com.birthday.ui.birthdayFeed

import android.text.InputType
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withInputType
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.birthday.ui.R
import com.birthday.ui.activity.BirthDayActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BirthdayAddFragmentTest{

  @Rule
  @JvmField
  var activityActivityTestRule = ActivityTestRule(BirthDayActivity::class.java)

  @Before
  fun setUp() {
      onView(withId(R.id.add)).perform(ViewActions.click())
  }

  @Test
  fun testProfileImage(){
    onView(withId(R.id.profile_image))
      .check(matches(isDisplayed()))
  }

  @Test
  fun tes_dobInputType(){
    onView(withId(R.id.dob_input)).check(matches(withInputType(0x14)))
  }

  @Test
  fun tes_dobSucess(){
    onView(withId(R.id.dob_input)).perform(clearText(), typeText("12/10/1986"))
    onView(withId(R.id.dob_input)).check(matches(withText("12/10/1986")))
  }

  @Test
  fun tes_dobfailed(){
    onView(withId(R.id.dob_input)).perform(clearText(), typeText("failed"))
    onView(withId(R.id.dob_input)).check(matches(withText("DD/MM/YYYY")))
  }

  @Test
  fun testProfileName(){
    onView(withId(R.id.nameInput))
      .check(matches(isDisplayed()))
  }

}
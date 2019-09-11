package com.birthday.ui.birthdayFeed

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
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
      .check(ViewAssertions.matches(isDisplayed()))
  }

  @Test
  fun testProfileName(){
    onView(withId(R.id.nameInput))
      .check(ViewAssertions.matches(isDisplayed()))
  }

}
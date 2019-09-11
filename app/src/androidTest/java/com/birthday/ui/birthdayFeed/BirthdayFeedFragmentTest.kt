package com.birthday.ui.birthdayFeed

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.birthday.ui.R
import com.birthday.ui.activity.BirthDayActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BirthdayFeedFragmentTest{
  @Rule
  @JvmField
  var activityActivityTestRule = ActivityTestRule(BirthDayActivity::class.java)

  @Before
  fun setUp() {
    runOnUiThread {
      activityActivityTestRule.activity.setFragment(BirthdayFeedFragment.newInstance())
    }
  }

  @Test
  fun test_ProfileImage(){
    onView(withId(R.id.searchView))
      .check(matches(isDisplayed()))
  }
  @Test
  fun test_BirthdayToolbarIsDisplayed(){
    onView(withId(R.id.toolbar)).check(matches(ViewMatchers.isDisplayed()))
  }

  @Test
  fun test_menuContainAddItem(){
    onView(withId(R.id.add)).check(matches(ViewMatchers.isDisplayed()))
  }

  @Test
  fun test_profile_imageInAddFragment(){
    onView(withId(R.id.add)).perform(ViewActions.click())
    onView(withId(R.id.profile_image)).check(matches(ViewMatchers.isDisplayed()))
  }

  @Test
  fun test_BirthdayTitleIsDisplayed(){
    onView(withId(R.id.birthdaytitle)).check(matches(ViewMatchers.isDisplayed()))
  }

  @Test
  fun test_BirthTitleDisplayCorrectText(){
    onView(withId(R.id.birthdaytitle)).check(matches(ViewMatchers.withText(R.string.birthdayTitle)))
  }

  @Test
  fun test_BirthdaysearchViewIsDisplayed(){
    onView(withId(R.id.searchView)).check(matches(ViewMatchers.isDisplayed()))
  }

  @Test
  fun test_Birthday_info_listDisplayed(){
    onView(withId(R.id.birthday_info_list)).check(matches(ViewMatchers.isDisplayed()))
  }
}
package com.birthday.ui.activity

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.birthday.ui.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BirthDayActivityTest{

  @Rule
  @JvmField
  var activityActivityTestRule = ActivityTestRule(BirthDayActivity::class.java)

  var testContext = InstrumentationRegistry.getInstrumentation().getContext()
  var testRes = testContext.getResources()

  @Test
  fun testContainer(){
    onView(withId(R.id.container)).check(matches(isDisplayed()))
  }
}
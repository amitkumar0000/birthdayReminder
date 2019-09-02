package com.birthday.ui.birthdayFeed.di

import com.birthday.ui.birthdayFeed.BirthdayAddFragment
import com.birthday.ui.birthdayFeed.BirthdayFeedFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class,BirthdayStorageModule::class])
interface BirthdayStorageComponent {
  fun inject(birthdayFeedFragment : BirthdayFeedFragment)
  fun inject(addFragment: BirthdayAddFragment)

}
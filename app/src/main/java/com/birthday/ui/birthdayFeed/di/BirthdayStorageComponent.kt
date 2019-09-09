package com.birthday.ui.birthdayFeed.di

import com.birthday.di.modules.ContextModule
import com.birthday.ui.birthdayFeed.BirthdayAddFragment
import com.birthday.ui.birthdayFeed.BirthdayFeedFragment
import com.birthday.ui.fragment.BirthdayDetailsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class,BirthdayStorageModule::class])
interface BirthdayStorageComponent {
  fun inject(birthdayFeedFragment : BirthdayFeedFragment)
  fun inject(addFragment: BirthdayAddFragment)
  fun inject(addFragment: BirthdayDetailsFragment)

}
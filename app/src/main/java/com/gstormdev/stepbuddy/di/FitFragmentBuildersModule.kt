package com.gstormdev.stepbuddy.di

import com.gstormdev.stepbuddy.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FitFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributesMainFragment(): MainFragment
}
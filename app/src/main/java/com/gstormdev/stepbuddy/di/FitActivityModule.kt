package com.gstormdev.stepbuddy.di

import com.gstormdev.stepbuddy.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FitActivityModule {

    @ContributesAndroidInjector(modules = [FitFragmentBuildersModule::class])
    abstract fun contributesMainActivity(): MainActivity
}
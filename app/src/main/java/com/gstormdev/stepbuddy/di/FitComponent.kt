package com.gstormdev.stepbuddy.di

import com.gstormdev.stepbuddy.ui.main.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FitModule::class])
interface FitComponent {
    fun inject(vm: MainViewModel)
}
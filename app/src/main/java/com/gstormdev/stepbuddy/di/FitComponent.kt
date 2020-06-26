package com.gstormdev.stepbuddy.di

import com.gstormdev.stepbuddy.StepApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, FitModule::class, FitActivityModule::class])
interface FitComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: StepApplication): Builder

        fun build(): FitComponent
    }

    fun inject(app: StepApplication)
}
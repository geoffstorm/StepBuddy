package com.gstormdev.stepbuddy

import android.app.Application
import android.content.Context
import com.gstormdev.stepbuddy.di.DaggerFitComponent
import com.gstormdev.stepbuddy.di.FitComponent
import com.gstormdev.stepbuddy.di.FitModule

class StepApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context

        val appComponent: FitComponent by lazy {
            DaggerFitComponent.builder()
                    .fitModule(FitModule(context))
                    .build()
        }
    }
}
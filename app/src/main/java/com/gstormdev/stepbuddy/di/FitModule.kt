package com.gstormdev.stepbuddy.di

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.gstormdev.stepbuddy.StepApplication
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class FitModule {

    @Provides
    fun provideFitnessOptions(): FitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()

    @Singleton
    @Inject
    @Provides
    fun provideGoogleAccount(app: Application, options: FitnessOptions): GoogleSignInAccount = GoogleSignIn.getAccountForExtension(app.applicationContext, options)

    @Singleton
    @Provides
    fun providesApplication(app: StepApplication): Application { return app }
}
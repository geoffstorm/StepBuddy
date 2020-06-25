package com.gstormdev.stepbuddy.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class FitModule(private val context: Context) {

    @Provides
    fun provideFitnessOptions(): FitnessOptions = FitnessOptions.builder()
        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
        .build()

    @Singleton
    @Inject
    @Provides
    fun provideGoogleAccount(options: FitnessOptions): GoogleSignInAccount = GoogleSignIn.getAccountForExtension(context, options)
}
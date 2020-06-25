package com.gstormdev.stepbuddy.ui.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.gstormdev.stepbuddy.StepApplication
import com.gstormdev.stepbuddy.model.StepHistory
import com.gstormdev.stepbuddy.util.endOfDay
import com.gstormdev.stepbuddy.util.startOfDay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel : ViewModel() {
    @Inject
    lateinit var fitnessOptions: FitnessOptions

    @Inject
    lateinit var googleAccount: GoogleSignInAccount

    private val _stepHistory = MutableLiveData<List<StepHistory>>(emptyList())
    val stepHistory: LiveData<List<StepHistory>> = _stepHistory

    init {
        StepApplication.appComponent.inject(this)
    }

    fun checkFitPermissions(fragment: Fragment, requestCode: Int) {
        if (!GoogleSignIn.hasPermissions(googleAccount, fitnessOptions)) {
            GoogleSignIn.requestPermissions(fragment, requestCode, googleAccount, fitnessOptions)
        } else {
            // Would be nice to have some caching system so we don't have to make the request every time
            retrieveFitData()
        }
    }

    fun retrieveFitData() {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val endTime = cal.endOfDay().timeInMillis
        cal.add(Calendar.MONTH, -3)
        val startTime = cal.startOfDay().timeInMillis

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        Fitness.getHistoryClient(StepApplication.context, googleAccount)
            .readData(readRequest)
            .addOnSuccessListener { response ->
                // listener is run on the main thread, so spawn a coroutine to stay off of it
                viewModelScope.launch {
                    val history = response.buckets.flatMap { it.dataSets }.flatMap { it.dataPoints }
                            .map {
                                StepHistory(it.getStartTime(TimeUnit.MILLISECONDS), it.getValue(Field.FIELD_STEPS).asInt())
                            }
                            .sortedByDescending { it.dateTime }
                    _stepHistory.postValue(history)
                }
            }
            .addOnFailureListener { exception ->
                // TODO
            }
    }
}
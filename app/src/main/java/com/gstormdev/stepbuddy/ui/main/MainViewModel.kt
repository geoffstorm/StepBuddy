package com.gstormdev.stepbuddy.ui.main

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.DataReadRequest
import com.gstormdev.stepbuddy.StepApplication
import com.gstormdev.stepbuddy.util.endOfDay
import com.gstormdev.stepbuddy.util.startOfDay
import java.text.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel : ViewModel() {
    @Inject
    lateinit var fitnessOptions: FitnessOptions

    @Inject
    lateinit var googleAccount: GoogleSignInAccount

    init {
        StepApplication.appComponent.inject(this)
    }

    fun checkFitPermissions(fragment: Fragment, requestCode: Int) {
        if (!GoogleSignIn.hasPermissions(googleAccount, fitnessOptions)) {
            GoogleSignIn.requestPermissions(fragment, requestCode, googleAccount, fitnessOptions)
        } else {
            accessFitData()
        }
    }

    fun accessFitData() {
        val cal = Calendar.getInstance()
        cal.time = Date()
        val endTime = cal.endOfDay().timeInMillis
        cal.add(Calendar.DATE, -3)
        val startTime = cal.startOfDay().timeInMillis

        val readRequest = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .bucketByTime(1, TimeUnit.DAYS)
            .build()

        Fitness.getHistoryClient(StepApplication.context, googleAccount)
            .readData(readRequest)
            .addOnSuccessListener { response ->
                // TODO
                response.buckets.map { bucket -> bucket.dataSets }.flatMap { it }.forEach { dumpDataSet(it) }  // to see what data is available for testing
                val stepList = response.buckets.flatMap { it.dataSets }.flatMap { it.dataPoints }.sortedByDescending { it.getStartTime(TimeUnit.MILLISECONDS) }.map { it.getValue(Field.FIELD_STEPS).asInt() }
                Log.e("MAIN VIEW MODEL",  "STEPS LIST: ${stepList.joinToString()}")
            }
            .addOnFailureListener { exception ->
                // TODO
            }
    }

    private fun dumpDataSet(dataSet: DataSet) {
        val TAG = "MainViewModel"
        Log.e(TAG, "Data returned for Data type: ${dataSet.dataType.name}")
        val format = DateFormat.getDateTimeInstance()
        for (point in dataSet.dataPoints) {
            Log.e(TAG, "Data point:")
            Log.e(TAG, "\tType: ${point.dataType.name}")
            Log.e(TAG, "\tStart: ${format.format(point.getStartTime(TimeUnit.MILLISECONDS))}")
            Log.e(TAG, "\tEnd ${format.format(point.getEndTime(TimeUnit.MILLISECONDS))}")
            for (field: Field in point.dataType.fields) {
                Log.e(TAG, "\tField: ${field.name} Value ${point.getValue(field)}")
            }
        }
    }
}
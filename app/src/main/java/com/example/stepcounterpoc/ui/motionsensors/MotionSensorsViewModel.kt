package com.example.stepcounterpoc.ui.motionsensors

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MotionSensorsViewModel : ViewModel() {

    companion object {
        val TAG = MotionSensorsViewModel::class.simpleName
    }

    private val _sensorValue = MutableLiveData<String>()
    val sensorValue: LiveData<String> = _sensorValue

    fun setValues(value: String) {
        _sensorValue.postValue(value)
    }
}
package com.example.stepcounterpoc.ui.googlefit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class GoogleFitViewModel : ViewModel() {

    companion object {
        val TAG = GoogleFitViewModel::class.simpleName
    }

    private val _sensorValue = MutableLiveData<String>()
    val sensorValue: LiveData<String> = _sensorValue

    fun setValues(value: String) {
        _sensorValue.postValue(value)
    }

    init {
        _sensorValue.value = "0"
    }
}
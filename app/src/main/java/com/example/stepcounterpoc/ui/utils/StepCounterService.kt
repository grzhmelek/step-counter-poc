package com.example.stepcounterpoc.ui.utils

import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var background = false

    companion object {

        private val TAG = StepCounterService::class.simpleName

        const val KEY_STEP_NUMBER = "stepNumber"
        const val KEY_BACKGROUND = "background"
        const val KEY_NOTIFICATION_ID = "notificationId"
        const val KEY_ON_SENSOR_CHANGED_ACTION = "ON_SENSOR_CHANGED"
        const val KEY_NOTIFICATION_STOP_ACTION = "NOTIFICATION_STOP"
        const val KEY_SENSOR_NOT_AVAILABLE_MESSAGE = "SENSOR_NOT_AVAILABLE"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        sensorManager = getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)?.also { countSensor ->
            sensorManager.registerListener(this,
                countSensor,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI)
        }

        val notification = createNotification("nothing counted", this)
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            background = it.getBooleanExtra(KEY_BACKGROUND, false)
        }

        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d(TAG, "onSensorChanged")
        if (event == null) {
            return
        }

        if (event.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            updateStepCounter(event.values.last().toInt().toString())

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //do nothing...
    }

    private fun updateStepCounter(value: String) {
        Log.d(TAG, "updateStepCounter")
        val intent = Intent()
        intent.putExtra(KEY_STEP_NUMBER, value)
        intent.action = KEY_ON_SENSOR_CHANGED_ACTION

        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        if (background) {
            startForeground(NOTIFICATION_ID, createNotification("Total steps: $value", this))
        } else {
            stopForeground(true)
        }
    }

    class ActionListener : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if (intent != null && intent.action != null) {
                if (intent.action.equals(KEY_NOTIFICATION_STOP_ACTION)) {
                    context?.let {
                        val notificationManager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val stepCounter = Intent(context, StepCounterService::class.java)
                        context.stopService(stepCounter)
                        val notificationId = intent.getIntExtra(KEY_NOTIFICATION_ID, -1)
                        if (notificationId != -1) {
                            notificationManager.cancel(notificationId)
                        }
                    }
                }
            }
        }
    }

    //TODO: return message if sensor is not available
    //   if (countSensor == null) {
//            Snackbar.make(binding.root,
//                "Count sensor not available",
//                Snackbar.LENGTH_LONG).show()
//        }
}
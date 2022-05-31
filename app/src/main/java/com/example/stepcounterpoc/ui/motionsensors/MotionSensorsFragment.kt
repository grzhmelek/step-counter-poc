package com.example.stepcounterpoc.ui.motionsensors

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.stepcounterpoc.databinding.FragmentMotionSensorsBinding
import com.example.stepcounterpoc.ui.utils.StepCounterService
import com.google.android.material.snackbar.Snackbar

class MotionSensorsFragment : Fragment() {

    companion object {
        val TAG = MotionSensorsFragment::class.simpleName
    }

    private lateinit var viewModel: MotionSensorsViewModel

    private lateinit var binding: FragmentMotionSensorsBinding

    private lateinit var sensorManager: SensorManager

    private var countSensor: Sensor? = null

    private var savedValue = 0

    private val requestPermissionLauncher =
        registerForActivityResult(RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.d(TAG, "isgranted")
                LocalBroadcastManager.getInstance(requireContext())
                    .registerReceiver(broadcastReceiver,
                        IntentFilter(StepCounterService.KEY_ON_SENSOR_CHANGED_ACTION))
            } else {
                Snackbar.make(binding.root,
                    "Activity recognition permission denied",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
        } else {
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(broadcastReceiver,
                IntentFilter(StepCounterService.KEY_ON_SENSOR_CHANGED_ACTION))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMotionSensorsBinding.inflate(inflater)

        viewModel = ViewModelProvider(this)[MotionSensorsViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        startForegroundServiceForSensors(false)
    }

    override fun onPause() {
        super.onPause()
        startForegroundServiceForSensors(true)
    }

    private fun startForegroundServiceForSensors(background: Boolean) {
        Log.d(TAG, "startForegroundServiceForSensors")
        val stepCounterIntent = Intent(requireActivity(), StepCounterService::class.java)
        stepCounterIntent.putExtra(StepCounterService.KEY_BACKGROUND, background)
        ContextCompat.startForegroundService(requireContext(), stepCounterIntent)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "onReceive")
            intent.getStringExtra(StepCounterService.KEY_STEP_NUMBER)
                ?.let { viewModel.setValues(it) }
        }
    }
}
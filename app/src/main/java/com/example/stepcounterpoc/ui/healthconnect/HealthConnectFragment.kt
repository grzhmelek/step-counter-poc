package com.example.stepcounterpoc.ui.healthconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthDataRequestPermissions
import androidx.health.connect.client.permission.Permission
import androidx.health.connect.client.records.Steps
import androidx.lifecycle.lifecycleScope
import com.example.stepcounterpoc.databinding.FragmentHealthConnectBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class HealthConnectFragment : Fragment() {

    private lateinit var binding: FragmentHealthConnectBinding

    // build a set of permissions for required data types
    private val PERMISSIONS =
        setOf(
            Permission.createReadPermission(Steps::class),
            Permission.createWritePermission(Steps::class)
        )

    // Create the permissions launcher.
    private val requestPermissions =
        registerForActivityResult(HealthDataRequestPermissions()) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions successfully granted
            } else {
                Snackbar.make(binding.root, "Permission denied", Snackbar.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHealthConnectBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (HealthConnectClient.isAvailable(requireActivity())) {
            // Health Connect is available and installed.
            val healthConnectClient = HealthConnectClient.getOrCreate(requireActivity())
            checkPermissionsAndRun(healthConnectClient)
        } else {
            Snackbar.make(binding.root, "Health Connect is not available", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun checkPermissionsAndRun(client: HealthConnectClient) {
        lifecycleScope.launch {
            val granted = client.permissionController.getGrantedPermissions(PERMISSIONS)
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions already granted
            } else {
                requestPermissions.launch(PERMISSIONS)
            }
        }
    }
}
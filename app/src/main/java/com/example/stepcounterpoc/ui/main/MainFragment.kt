package com.example.stepcounterpoc.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stepcounterpoc.R
import com.example.stepcounterpoc.databinding.FragmentMainBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.motionSensorsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_motionSensorsFragment)
        }

        binding.healthConnectButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_healthConnectFragment)
        }

        binding.googleFitButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_googleFitFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.contacts.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SplashFragment : Fragment() {

    lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        runBlocking {
            launch {
                delay(3000L)

                val isPermissionGranted: Boolean = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED

                if (!isPermissionGranted) {

                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        0
                    )

                    val permReqLuncher =
                        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                            if (it) {
                                findNavController().navigate(R.id.action_splashFragment_to_displayContactsListFragment)
                            } else {
                                requireActivity().finish()
                            }
                        }

                } else findNavController().navigate(R.id.action_splashFragment_to_displayContactsListFragment)


            }
        }
    }


}
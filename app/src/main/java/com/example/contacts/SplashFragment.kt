package com.example.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.contacts.databinding.FragmentSplashBinding


class SplashFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {

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

        val isPermissionGranted = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (!isPermissionGranted) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                REQ_MULTIPLE_PERMISSIONS_CODE
            )
        }
        else {
            GoToDisplayContactsListFrag()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            displayToastMessage("Granted both permissions")
            GoToDisplayContactsListFrag()

        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
            displayToastMessage("Read Contacts permissions granted, Write Contacts permission denied")
        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            displayToastMessage("Read Contacts permission denied , Write Contacts permission granted")
        } else {
            displayToastMessage("Both the permissions are denied")
            activity?.finish()
        }

    }

    private fun displayToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun GoToDisplayContactsListFrag() {
        findNavController().navigate(R.id.action_splashFragment_to_displayContactsListFragment)
    }

    companion object {
        private const val REQ_MULTIPLE_PERMISSIONS_CODE = 100

    }
}
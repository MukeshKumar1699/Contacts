package com.example.contacts.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.Contacts
import com.example.contacts.ContactsAdapter
import com.example.contacts.R
import com.example.contacts.databinding.FragmentDisplayContactsListBinding
import com.example.contacts.listeners.ContactsItemClickListener
import com.example.contacts.sharedpref.PreferenceHelper
import com.example.contacts.support.AppApplication
import com.example.contacts.support.SwipeGesture
import com.example.contacts.viewmodels.DisplayContactListViewModelFactory
import com.example.contacts.viewmodels.DisplayContactsListViewModel
import java.util.*
import kotlin.collections.ArrayList


class DisplayContactsListFragment : Fragment(), ContactsItemClickListener {

    lateinit var binding: FragmentDisplayContactsListBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val DBSTORESTATUS = "DBSTORE"

    private val sharedPreferences = PreferenceHelper()

    private val viewModel: DisplayContactsListViewModel by viewModels {
        DisplayContactListViewModelFactory((activity?.application as AppApplication).displayContactsListRepository)
    }
    private lateinit var contactsAdapter: ContactsAdapter

    private var contactsList: ArrayList<Contacts> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate: ")
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ")
        binding = FragmentDisplayContactsListBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAppPermission()

        linearLayoutManager = LinearLayoutManager(requireContext())
        Log.d(TAG, "onViewCreated: ")
        initViews()
        setRecycler()
        observeLiveData()
    }

    private fun getAppPermission() {

        val isPermissionGranted = checkPermission()

        if (isPermissionGranted && !sharedPreferences.getDBStoreStatusFromPreference(
                requireContext(),
                DBSTORESTATUS
            )
        ) {
            getContactsFromContentProviderAndNotifySharedPref()

        } else if (!isPermissionGranted) {
            requestPermissions(
                permissionList,
                REQ_MULTIPLE_PERMISSIONS_CODE
            )
        }

    }

    private fun checkPermission(): Boolean {

        for (i in permissionList) {

            if (checkSelfPermission(
                    requireContext(),
                    i
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQ_MULTIPLE_PERMISSIONS_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                getContactsFromContentProviderAndNotifySharedPref()

            } else {
                Toast.makeText(
                    requireContext(),
                    "Read & Write Contact Permission Required",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    private fun getContactsFromContentProviderAndNotifySharedPref() {

        viewModel.getContacts(requireActivity().contentResolver)
        sharedPreferences.writeDBStoreStatusToPreference(
            requireContext(),
            DBSTORESTATUS,
            true
        )
    }


    @SuppressLint("Range", "Recycle")
    private fun initViews() {

        sharedPreferences.getSharedPreference(requireContext())


        binding.flbAddContact.setOnClickListener {
//            val action =
//                DisplayContactsListFragmentDirections
//                    .actionDisplayContactsListFragmentToAddContactFragment()
//            openFragment(action)

            findNavController().navigate(R.id.action_displayContactsListFragment_to_addContactFragment)
        }

        binding.toolbarCL.tvTitle.apply {
            visibility = View.VISIBLE
        }
        binding.toolbarCL.tvSearchContact.apply {
            visibility = View.VISIBLE
        }.setOnClickListener {

            val action =
                DisplayContactsListFragmentDirections
                    .actionDisplayContactsListFragmentToSearchContactFragment()

            navigate(action)
        }
    }

    private fun setRecycler() {

        contactsAdapter = ContactsAdapter(contactsList, this)

        val dividerItemDecoration =
            DividerItemDecoration(
                binding.rcViewDisplayContactsList.getContext(),
                linearLayoutManager.getOrientation()
            )
        binding.rcViewDisplayContactsList.apply {

            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(dividerItemDecoration)
            this.adapter = contactsAdapter

            val swipeGesture = object : SwipeGesture(requireContext()) {

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    when (direction) {

                        //Right to Left
                        ItemTouchHelper.LEFT -> {
                            adapter?.notifyItemChanged(viewHolder.adapterPosition)
                            confirmDeleteAlert(viewHolder.adapterPosition)
                            adapter?.notifyItemChanged(viewHolder.adapterPosition)

                        }
                        //Left to Right
                        ItemTouchHelper.RIGHT -> {
                            adapter?.notifyItemChanged(viewHolder.adapterPosition)
                            editContact(viewHolder.adapterPosition)
                            adapter?.notifyItemChanged(viewHolder.adapterPosition)
                        }
                    }
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeGesture)
            itemTouchHelper.attachToRecyclerView(this)
        }

    }

    private fun observeLiveData() {
        viewModel.contactsLiveData.observe(requireActivity(), {
            Log.d(TAG, "observeLiveData: ${it.size}")
            contactsList.clear()
            contactsList.addAll(it)
            Log.d(TAG, "observeLiveData: ${contactsList.size}")
            sortAlphabetically(contactsList)
//            contactsList.sortedWith(
//                compareBy(
//                    String.CASE_INSENSITIVE_ORDER,
//                    { it.name.toString() })
//            )
//            contactsList.sortedWith(myCustomComparator)
            updateRecyclerView()
        })
    }

    fun sortAlphabetically(arrayList: ArrayList<Contacts>): ArrayList<Contacts> {
        var returnList: ArrayList<Contacts> = arrayListOf()
        var list = arrayList as MutableList<Contacts>
        list.sortWith(Comparator { o1: Contacts, o2: Contacts ->
            o1.name.compareTo(o2.name)
        })
        returnList = list as ArrayList<Contacts>
        return returnList
    }

    private fun updateRecyclerView() {
        binding.pbLoadingCL.apply {
            visibility = View.GONE
        }
        contactsAdapter.updateData(contactsList)
        Log.d(TAG, "updateRecyclerView: ${contactsList.size}")
    }


    private fun editContact(adapterPosition: Int) {

        val contact = contactsList[adapterPosition]

        val action =
            DisplayContactsListFragmentDirections
                .actionDisplayContactsListFragmentToAddContactFragment(contactsList[adapterPosition])
        openFragment(action)
    }

    private fun confirmDeleteAlert(adapterPosition: Int) {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setMessage("Sure to permanently Delete the contact?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                deleteContact(adapterPosition)
            }
            .setNegativeButton(
                "No"
            ) { dialog, id ->
                dialog.cancel()
            }

        val alert = alertDialogBuilder.create()
        alert.show()
    }

    private fun deleteContact(adapterPosition: Int) {
        Toast.makeText(requireContext(), "Delete Is called", Toast.LENGTH_SHORT).show()
        val contactToDELETE = contactsList[adapterPosition]
        viewModel.deleteContactFromDB(contactToDELETE)
    }

    private fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }


    override fun onItemClicked(position: Int, contacts: Contacts) {
        val action =
            DisplayContactsListFragmentDirections
                .actionDisplayContactsListFragmentToDisplayContactDetailsFragment(contacts)
        openFragment(action)
    }

    fun openFragment(action: NavDirections) {
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach: ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: ")
    }

    companion object {
        private const val TAG = "DisplayContactsListFrag"
        private val myCustomComparator = Comparator<Contacts> { a, b ->
            when {
                (a.name == null && b.name == null) -> 0
                (a.name == null) -> -1
                (b.name == null) -> 1
                else -> 0
            }
        }
        private val permissionList = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        private const val REQ_MULTIPLE_PERMISSIONS_CODE = 100
    }
}
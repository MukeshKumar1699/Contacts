package com.example.contacts.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.Contacts
import com.example.contacts.ContactsAdapter
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.databinding.FragmentSearchContactBinding
import com.example.contacts.listeners.ContactsItemClickListener
import com.example.contacts.support.AppApplication
import com.example.contacts.viewmodels.SearchContactViewModelFactory
import com.example.contacts.viewmodels.SearchContactsViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class SearchContactFragment : Fragment(), ContactsItemClickListener {

    lateinit var binding: FragmentSearchContactBinding

    private val viewModel: SearchContactsViewModel by viewModels {
        SearchContactViewModelFactory((activity?.application as AppApplication).searchContactRepository)
    }
    private lateinit var contactsAdapter: ContactsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setRecyclerView(contactsList)
        observeLiveData()

    }

    fun updateRecyclerView(searchResultList: List<Contacts>) {
        binding.pbLoadingSC.visibility = View.GONE
        contactsAdapter.updateData(searchResultList.toCollection(ArrayList()))
    }

    private fun setRecyclerView(contactsList: ArrayList<Contacts>) {

        contactsAdapter = ContactsAdapter(contactsList, this)
        binding.rcViewSearchContactsList.apply {

            this.layoutManager = LinearLayoutManager(context)
            this.adapter = contactsAdapter
        }
    }

    @SuppressLint("Range", "Recycle")
    private fun init() {

        binding.toolbarSC.ivBack.apply {
            visibility = View.VISIBLE
        }.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbarSC.svSearchContact.apply {
            visibility = View.VISIBLE
        }.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                query?.let {
                    binding.pbLoadingSC.visibility = View.VISIBLE
                    searchResultList.clear()
                    getSearchResult(it)
                    updateRecyclerView(searchResultList.toList())
                }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {

                query?.let {
                    binding.pbLoadingSC.visibility = View.VISIBLE
                    searchResultList.clear()
                    getSearchResult(it)
                    updateRecyclerView(searchResultList.toList())
                }

                return false
            }
        })
    }

    private fun getSearchResult(query: String) {


        searchByContactName(query)
        searchByContactNumber(query)
        searchByContactMail(query)

        Log.d(TAG, "getSearchResult: ${searchResultList.size}")
    }

    private fun searchByContactMail(query: String) {
        val tempEmailList: ArrayList<EmailDetails> = arrayListOf()

        for (email in emailList) {

            if (email.mailID.contains(query, ignoreCase = true)) {
                tempEmailList.add(email)
            }
        }

        for (email in tempEmailList) {
            for (contact in contactsList) {

                if (contact.userId.equals(email.contactId)) {
                    searchResultList.add(contact)
                }
            }
        }

    }

    private fun searchByContactNumber(query: String) {

        val tempPhoneList: ArrayList<PhoneDetails> = arrayListOf()

        for (phone in phoneList) {

            if (phone.number.contains(query, ignoreCase = true)) {
                tempPhoneList.add(phone)
            }
        }

        for (phone in tempPhoneList) {
            for (contact in contactsList) {

                if (contact.userId.equals(phone.contactId)) {
                    searchResultList.add(contact)
                }
            }
        }

    }

    private fun searchByContactName(query: String) {
        for (i in contactsList) {
            if (i.name.contains(query, ignoreCase = true)) {
                searchResultList.add(i)
                Log.d(TAG, "getSearchResult: SearchList${i.name}")
            }
        }

    }

    private fun observeLiveData() {

        viewModel.contactLiveData.observe(requireActivity(), {

            contactsList = it as ArrayList<Contacts>

            setRecyclerView(contactsList)
        })
        viewModel.phoneLiveData.observe(requireActivity(), {
            phoneList = it as ArrayList<PhoneDetails>
        })
        viewModel.emailLiveData.observe(requireActivity(), {
            emailList = it as ArrayList<EmailDetails>
        })

    }

    override fun onItemClicked(position: Int, contacts: Contacts) {

        val action =
            SearchContactFragmentDirections
                .actionSearchContactFragmentToDisplayContactDetailsFragment(contacts)
        findNavController().navigate(action)

    }

    companion object {
        private const val TAG = "SearchContactFragment"

        private var contactsList: ArrayList<Contacts> = ArrayList()
        private var phoneList: ArrayList<PhoneDetails> = ArrayList()
        private var emailList: ArrayList<EmailDetails> = ArrayList()


        private var searchResultList: HashSet<Contacts> = hashSetOf()
    }
}
package com.example.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.FragmentDisplayContactsListBinding

class DisplayContactsListFragment : Fragment(), ContactsItemClickListener {

    lateinit var binding: FragmentDisplayContactsListBinding

    private lateinit var viewModel: DisplayContactsListViewModel
    private var contactsAdapter: ContactsAdapter? = null

    var contactsList = ArrayList<Contacts>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDisplayContactsListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setRecyclerView(contactsList)
        observeLiveData()

    }

    private fun setRecyclerView(contactsList: List<Contacts>) {

        if (contactsAdapter == null) {

            contactsAdapter =
                ContactsAdapter(contactList = contactsList as ArrayList<Contacts>, this)

            binding.rcViewDisplayContactsList.apply {

                this.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                this.layoutManager = LinearLayoutManager(requireContext())
                Log.d("layout", "layout manager called")
                this.adapter = contactsAdapter
            }
        } else {
            binding.pbLoading?.visibility = View.GONE
            contactsAdapter!!.updateData(contactsList as ArrayList<Contacts>)
        }
    }

    @SuppressLint("Range", "Recycle")
    private fun init() {

        viewModel =
            ViewModelProvider(requireActivity()).get(DisplayContactsListViewModel::class.java)
//            mViewModel = ViewModelProviders.of(this).get(CollabInfoViewModel::class.java)


        val contentResolver: ContentResolver = requireContext().contentResolver

        viewModel.getContacts(contentResolver)

        binding.flbAddContact.setOnClickListener {

            findNavController().navigate(R.id.action_displayContactsListFragment_to_addContactFragment)
        }

        binding.svSearchContact.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {

                if (query?.isNotEmpty() == true && query.length > 0) {

//                    viewModel.searchContact(query)
//                    contactsAdapter!!.updateData(findContactbyName(contactsList, query))
                }
                return false
            }


        })

    }

    fun findContactbyName(contactsList: ArrayList<Contacts>, name: String): ArrayList<Contacts> {

        val filteredList = ArrayList<Contacts>()
        for (contacts in contactsList) {

            if (contacts.name.equals(name)) {
                filteredList.add(contacts)
            }
        }
        return filteredList
    }

    private fun observeLiveData() {

        viewModel.liveData.observe(requireActivity(), {
            Log.d("Display", "observeLiveData: ${it.size}")
            setRecyclerView(it)

        })
    }

    override fun onItemClicked(position: Int, contacts: Contacts) {

//        val bundle = bundleOf("contacts" to contacts)
        findNavController().navigate(R.id.action_displayContactsListFragment_to_displayContactDetailsFragment)

    }

}
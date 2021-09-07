package com.example.contacts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.FragmentDisplayContactDetailsBinding


class DisplayContactDetailsFragment : Fragment() {

    lateinit var binding: FragmentDisplayContactDetailsBinding
    private var phoneAdapter: PhoneAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDisplayContactDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //getBundleData()
//        setRecyclerView(contacts.phoneList)

    }

    private fun setRecyclerView(phoneList: List<PhoneDetails>) {

        if (phoneAdapter == null) {

            phoneAdapter =
                PhoneAdapter(phoneList)

            binding.rcViewPhoneNumbers.apply {

                this.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                this.layoutManager = LinearLayoutManager(requireContext())

                this.adapter = phoneAdapter

            }
        }
//        else {
//            binding.pbLoading?.visibility = View.GONE
//            contactsAdapter!!.updateData(contactsList as ArrayList<Contacts>)
//        }
    }


    private fun getBundleData() {
        val bundle = arguments?.get("contacts")
//        contacts = bundle as Contacts
    }

}
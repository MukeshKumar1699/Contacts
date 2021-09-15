package com.example.contacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.FragmentDisplayContactDetailsBinding


class DisplayContactDetailsFragment : Fragment(), PhoneNumbersItemClickListener {

    private val args by navArgs<DisplayContactDetailsFragmentArgs>()
    lateinit var binding: FragmentDisplayContactDetailsBinding
    private var phoneAdapter: PhoneAdapter? = null
    private lateinit var contacts: Contacts

    private lateinit var viewModel: DisplayContactDetailsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDisplayContactDetailsBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {

        contacts = args.contact

        viewModel =
            ViewModelProvider(requireActivity()).get(DisplayContactDetailsViewModel::class.java)

        viewModel.getPhoneNumbersList(contactID = contacts.userId)

        binding.tvName.text = contacts.name

        if (!contacts.image.equals("")) binding.ivContactImage.setImageURI(Uri.parse(contacts.image))

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.phoneDetailsLiveData.observe(requireActivity(), {

            setRecyclerView(it)
            binding.pbLoading.visibility = View.GONE
        })

    }


    private fun setRecyclerView(phoneList: List<PhoneDetails>) {

        if (phoneAdapter == null) {

            phoneAdapter =
                PhoneAdapter(phoneList, this)

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

    }

    override fun onItemClicked(position: Int, phoneDetails: PhoneDetails, action: Int) {

        when (action) {
            1 -> call(phoneDetails.number)
            2 -> sendSMS(phoneDetails.number)
//            3 -> videoCall(phoneDetails.number)
        }
    }

    fun call(number: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$number")
        startActivity(dialIntent)
    }

    fun sendSMS(number: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)))
    }

    fun videoCall(number: String) {

        val intent = Intent()
        intent.setPackage("com.google.android.apps.tachyon")
        intent.action = "com.google.android.apps.tachyon.action.CALL"
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }


}
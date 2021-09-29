package com.example.contacts.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.*
import com.example.contacts.databinding.FragmentDisplayContactDetailsBinding
import com.example.contacts.listeners.EmailItemClickListener
import com.example.contacts.listeners.PhoneNumbersItemClickListener
import com.example.contacts.support.AppApplication
import com.example.contacts.viewmodels.DisplayContactDetailsViewModel
import com.example.contacts.viewmodels.DisplayContactDetailsViewModelFactory


class DisplayContactDetailsFragment : Fragment(), PhoneNumbersItemClickListener,
    EmailItemClickListener {

    private val args by navArgs<DisplayContactDetailsFragmentArgs>()
    private lateinit var binding: FragmentDisplayContactDetailsBinding
    private var phoneAdapter: PhoneAdapter? = null
    private var emailAdapter: EmailAdapter? = null

    private lateinit var contacts: Contacts

    private val viewModel: DisplayContactDetailsViewModel by viewModels {
        DisplayContactDetailsViewModelFactory((activity?.application as AppApplication).displayContactsDetailsRepository)
    }

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
        phoneAdapter = null
        emailAdapter = null
        viewModel.getPhoneNumbersList(contactID = contacts.userId)
        viewModel.getEmailList(contactID = contacts.userId)

        binding.tvName.text = contacts.name

        binding.toolbarCD.ivBack.apply {
            visibility = View.VISIBLE
        }.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbarCD.ivStar.apply {
            visibility = View.VISIBLE
        }
        binding.toolbarCD.ivMore.apply {
            visibility = View.VISIBLE
        }
        binding.btnPcall.apply { }.setOnClickListener {
            call(phoneNumbersList[0].number)
        }
        binding.btnPmessage.apply { }.setOnClickListener {
            sendSMS(phoneNumbersList[0].number)
        }
        binding.fabEdit.apply {

        }.setOnClickListener {
            val action =
                DisplayContactDetailsFragmentDirections
                    .actionDisplayContactDetailsFragmentToAddContactFragment(contacts)
            findNavController().navigate(action)
        }
        binding.tvContactInitial.text = contacts.name?.get(0).toString()
        binding.sivContactImage.setBackgroundColor(contacts.color.toInt())

//        if (!contacts.image.equals("null")) binding.ivContactImage.setImageURI(Uri.parse(contacts.image))

        observeLiveData()
    }

    private fun observeLiveData() {

        viewModel.phoneLiveData.observe(requireActivity(), {
            phoneNumbersList = it
            setPhoneRecyclerView(phoneNumbersList)
            binding.pbLoading.visibility = View.GONE
        })

        viewModel.emailLiveData.observe(requireActivity(), {
            emailList = it
            setEmailRecyclerView(emailList)
            binding.pbLoading.visibility = View.GONE
        })
    }

    private fun setPhoneRecyclerView(phoneList: List<PhoneDetails>) {

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

    private fun setEmailRecyclerView(emailList: List<EmailDetails>) {

        if (emailAdapter == null) {

            emailAdapter =
                EmailAdapter(emailList, this)

            binding.rcViewEmail.apply {

                this.addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.VERTICAL
                    )
                )
                this.layoutManager = LinearLayoutManager(requireContext())
                this.adapter = emailAdapter

            }
        }

    }


    private fun call(number: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$number")
        startActivity(dialIntent)
    }

    private fun sendSMS(number: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number, null)))
    }

    fun videoCall(number: String) {

        val intent = Intent()
        intent.setPackage("com.google.android.apps.tachyon")
        intent.action = "com.google.android.apps.tachyon.action.CALL"
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    private fun sendEmail(mailId: String) {

        displayToast("Send EMail Clicked")
        val intent = Intent(Intent.ACTION_SEND)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, mailId)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    fun displayToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClicked(position: Int, phoneDetails: PhoneDetails, action: Int) {

        when (action) {
            1 -> call(phoneDetails.number)
            2 -> sendSMS(phoneDetails.number)
//            3 -> videoCall(phoneDetails.number)
        }
    }

    override fun onItemClicked(position: Int, emailDetails: EmailDetails) {
        sendEmail(emailDetails.mailID)
    }

    companion object {
        private var phoneNumbersList: List<PhoneDetails> = arrayListOf()
        private var emailList: List<EmailDetails> = arrayListOf()
    }


}
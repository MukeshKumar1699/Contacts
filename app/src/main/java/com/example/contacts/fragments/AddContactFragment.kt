package com.example.contacts.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.R
import com.example.contacts.databinding.FragmentAddContactBinding
import com.example.contacts.sharedpref.PreferenceHelper
import com.example.contacts.support.AddContactToContentProvider
import com.example.contacts.support.AppApplication
import com.example.contacts.viewmodels.AddContactViewModel
import com.example.contacts.viewmodels.AddContactViewModelFactory
import com.google.android.material.textfield.TextInputEditText
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap


class AddContactFragment : Fragment() {

    private lateinit var addContactToContentProvider: AddContactToContentProvider

    private var imageUri: Bitmap? = null
    private val CONTACTID = "CONTACTID"
    lateinit var binding: FragmentAddContactBinding

    private val viewModel: AddContactViewModel by viewModels {
        AddContactViewModelFactory((activity?.application as AppApplication).addContactRepository)
    }

    private val sharedPreferences = PreferenceHelper()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: ")

        addContactToContentProvider = AddContactToContentProvider(requireContext().contentResolver)
        val args: AddContactFragmentArgs = AddContactFragmentArgs.fromBundle(requireArguments())
        init(args)

        binding = FragmentAddContactBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")

        sharedPreferences.getSharedPreference(requireContext())
        initViews()


    }

    private fun initViews() {

        binding.apply {
            if (isAddorEditContact.equals("Edit")) {
                binding.tetName.setText(contact.name)

                if (!contact.image.equals("null")) {
                    binding.sivContactImage.setImageURI(Uri.parse(contact.image))
                }
                for (phoneDetails in phoneNumbersList) {
                    addPhoneToView(phoneDetails)
                }
                for (emailDetails in emailList) {
                    addEmailToView(emailDetails)
                }

            } else if (isAddorEditContact.equals("Add")) {
                binding.tetName.setText("")
                addPhoneToView(PhoneDetails("", "", ""))
                addEmailToView(EmailDetails("", "", ""))
            }

            binding.btnAddPhone.setOnClickListener {
                addPhoneToView(PhoneDetails("", "", ""))
            }

            binding.btnAddEmail.setOnClickListener {
                addEmailToView(EmailDetails("", "", ""))
            }

            binding.toolbarAC.tvTitle.apply {
                visibility = View.VISIBLE
                if (isAddorEditContact.equals("Add")) text = getString(R.string.create_contact)
                else if (isAddorEditContact.equals("Edit")) text = getString(R.string.edit_contact)
            }

            binding.toolbarAC.ivClose.apply {
                visibility = View.VISIBLE
            }.setOnClickListener {
                findNavController().popBackStack()
            }

            binding.toolbarAC.ivMore.apply {
                visibility = View.VISIBLE
            }

            binding.sivContactImage.apply { }.setOnClickListener {

                chooseContactImage()

            }

            binding.toolbarAC.tvSave.apply {
                visibility = View.VISIBLE
            }.setOnClickListener {
                if (isAddorEditContact.equals("Add")) {

                    displayToast("Save Clicked")
                    val name = binding.tipFirstName.editText?.text.toString()


                    if (name.isNotEmpty()) {

                        contactId = sharedPreferences.getContactIDUpdatedFromPreference(
                            requireContext(),
                            CONTACTID
                        )
                            .toString()
                        contact = Contacts(contactId, name, imageUri.toString())
                        contact.color = createColor().toString()

                        if (getPhoneNumbersFromView() && getEmailIdFromView()) {
                            addContactToDB(contact, phoneNumbersList.toList(), emailList.toList())
                            findNavController().popBackStack()
                        }

                    } else {
                        binding.tipFirstName.error = "Name Must not be null"
                    }
                } else if (isAddorEditContact.equals("Edit")) {

                    contact.name = binding.tipFirstName.editText?.text.toString()

                    if (contact.name.isNotEmpty()) {

                        phoneNumbersList.clear()
                        emailList.clear()

                        if (getPhoneNumbersFromView() && getEmailIdFromView()) {
                            editContact()
                            findNavController().popBackStack()
                        }

                    } else {
                        binding.tipFirstName.error = "Name Must not be Null"
                    }
                }
            }
        }
    }

    private fun chooseContactImage() {

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val layoutInflater = layoutInflater
        val dialogView = layoutInflater.inflate(R.layout.image_option,null)
        alertDialogBuilder.setMessage("Choose option to set contact Image")
            .setCancelable(false)
            .setView(dialogView)

        val galleryIcon = dialogView.findViewById<ImageView>(R.id.gallery)
        val cameraIcon = dialogView.findViewById<ImageView>(R.id.camera)

        val alert = alertDialogBuilder.create()
        alert.show()

        galleryIcon.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
            alert.cancel()
        }
        cameraIcon.setOnClickListener {
            val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(camera.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(camera, CAPTURE_IMAGE)
                alert.cancel()
        }
        }

    }

    private fun getEmailIdFromView(): Boolean {
        for ((key, value) in emailHashmap) {

            val email = key.text.toString()
            val type = value.selectedItem.toString()


            if (email.isNotEmpty() && email.contains("@")) {
                val emailDetails = EmailDetails(email, type, contactId)
                Log.d(
                    TAG,
                    "onViewCreated: email: $email type: $type contactId: $contactId"
                )
                emailList.add(emailDetails)
            } else if (email.isNotEmpty() && !email.contains("@")) {
                key.error = "Invalid MailId"
                return false
            }
        }
        return true
    }

    private fun getPhoneNumbersFromView(): Boolean {
        for ((key, value) in phoneHashmap) {

            val number = key.text.toString()
            val type = value.selectedItem.toString()

            if (number.length >= 10) {
                val phoneDetails = PhoneDetails(number, type, contactId)
                Log.d(
                    TAG,
                    "onViewCreated: number: $number type: $type contactId: $contactId"
                )
                phoneNumbersList.add(phoneDetails)
            } else {
                key.error = "Invalid Number"
                return false
            }
        }
        return true
    }

    private fun editContact() {

        viewModel.editContact(contact, phoneNumbersList.toList(), emailList.toList())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {

            PICK_IMAGE -> {
                if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data?.data != null) {
//                    imageUri = data.data
//                    binding.sivContactImage.setImageURI(imageUri)
                }
            }
            CAPTURE_IMAGE -> {
                if(resultCode == RESULT_OK && requestCode == CAPTURE_IMAGE && data?.extras != null) {
                    val bundle = data.extras
                    imageUri = bundle?.get("data") as Bitmap
                    binding.sivContactImage.setImageBitmap(imageUri)
                }
            }
        }

    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    fun displayToast(message: String) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun addContactToDB(
        contacts: Contacts, phoneNumberList: List<PhoneDetails>, emailList: List<EmailDetails>
    ) {
        viewModel.addContact(contacts, phoneNumberList, emailList)
        sharedPreferences.writeContactIDUpdateToPreference(requireContext(), CONTACTID, contactId.toInt()+1)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
        phoneNumbersList.clear()
        isAddorEditContact = "Add"
        emailList.clear()
    }
    private fun observeLiveData() {

        viewModel.getPhoneNumbers().observe(requireActivity(), {
            phoneNumbersList.addAll(it)

            for (i in phoneNumbersList) {
                addPhoneToView(i)
                displayToast(i.sNo.toString())
            }
        })

        viewModel.getEmailIDs().observe(requireActivity(), {
            emailList.addAll(it)

            for (i in emailList) {
                addEmailToView(i)
            }
        })

    }

    fun createColor(): Int {

        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        return color
    }

    private fun addEmailToView(emailDetails: EmailDetails) {

        if (isAdded) {
            val addEmailView: View = layoutInflater.inflate(R.layout.field, null, false)

            val tetEmail = addEmailView.findViewById<TextInputEditText>(R.id.tet_PhoneorEmail)
            tetEmail.hint = "Email"
            tetEmail.setText(emailDetails.mailID)

            val spinner = addEmailView.findViewById<Spinner>(R.id.spin_type)
            when (emailDetails.type) {
                "HOME" -> spinner.setSelection(0)
                "MOBILE" -> spinner.setSelection(1)
                "WORK" -> spinner.setSelection(2)
                "CUSTOM" -> spinner.setSelection(3)
                "OTHER" -> spinner.setSelection(4)
            }

            val remove = addEmailView.findViewById<ImageView>(R.id.close)
            remove.setOnClickListener {
                binding.llCustomEmail.removeView(addEmailView)
                emailHashmap.remove(tetEmail)
            }

            emailHashmap.put(tetEmail, spinner)
            binding.llCustomEmail.addView(addEmailView)
        }

    }

    private fun addPhoneToView(phoneDetails: PhoneDetails) {

        if (isAdded) {

            val addPhoneView: View = layoutInflater.inflate(R.layout.field, null, false)

            val tetPhone = addPhoneView.findViewById<TextInputEditText>(R.id.tet_PhoneorEmail)
            tetPhone.hint = "Phone"
            tetPhone.setText(phoneDetails.number)

            val spinner = addPhoneView.findViewById<Spinner>(R.id.spin_type)
            when (phoneDetails.type) {
                "HOME" -> spinner.setSelection(0)
                "MOBILE" -> spinner.setSelection(1)
                "WORK" -> spinner.setSelection(2)
                "CUSTOM" -> spinner.setSelection(3)
                "OTHER" -> spinner.setSelection(4)
            }

            val remove = addPhoneView.findViewById<ImageView>(R.id.close)
            remove.setOnClickListener {
                binding.llCustomPhone.removeView(addPhoneView)
                phoneHashmap.remove(tetPhone)
            }

            phoneHashmap.put(tetPhone, spinner)
            binding.llCustomPhone.addView(addPhoneView)
        }
    }

    private fun init(args: AddContactFragmentArgs) {

        args.contact?.let {
            isAddorEditContact = "Edit"
            contact = it
            fetchLiveData()
            observeLiveData()
        }
    }

    private fun fetchLiveData() {
        viewModel.fetchEmailID(contact.userId)
        viewModel.fetchPhoneNumber(contact.userId)
    }

    companion object {

        val PICK_IMAGE: Int = 1
        val CAPTURE_IMAGE: Int = 2

        private lateinit var contact: Contacts
        private var phoneNumbersList: HashSet<PhoneDetails> = hashSetOf()
        private var emailList: HashSet<EmailDetails> = hashSetOf()

        private var isAddorEditContact = "Add"
        var contactId: String = "20000"

        var phoneHashmap: HashMap<EditText, Spinner> = HashMap()
        var emailHashmap: HashMap<EditText, Spinner> = HashMap()

        private const val TAG = "AddContactFragment"
    }

/*
private fun addContact(
 ctx: Context,
 displayName: String?,
 phoneNumberList: List<PhoneDetails>,
 emailList: List<EmailDetails>,

 ) {
 val ops: ArrayList<ContentProviderOperation> = ArrayList()

 ops.add(
     ContentProviderOperation.newInsert(
         ContactsContract.RawContacts.CONTENT_URI
     )
         .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
         .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
         .build()
 )

 //------------------------------------------------------ Names
 if (displayName != null) {
     ops.add(
         ContentProviderOperation.newInsert(
             ContactsContract.Data.CONTENT_URI
         )
             .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
             .withValue(
                 ContactsContract.Data.MIMETYPE,
                 ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
             )
             .withValue(
                 ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                 displayName
             ).build()
     )
 }

 //------------------------------------------------------ Mobile Number

 for (i in phoneNumberList) {

     var phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER

     when (i.type) {
         "HOME" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME
         "MOBILE" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
         "WORK" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK
         "CUSTOM" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM
         "OTHER" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
     }

     ops.add(
         ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
             .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
             .withValue(
                 ContactsContract.Data.MIMETYPE,
                 ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
             )
             .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, i.number)
             .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType)
             .build()
     )
 }

 //------------------------------------------------------ Email

 for (i in emailList) {

     var emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_OTHER

     when (i.type) {
         "HOME" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_HOME
         "MOBILE" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
         "WORK" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_WORK
         "CUSTOM" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM
         "OTHER" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_OTHER
     }


     ops.add(
         ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
             .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
             .withValue(
                 ContactsContract.Data.MIMETYPE,
                 ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
             )
             .withValue(ContactsContract.CommonDataKinds.Email.DATA, i.mailID)
             .withValue(ContactsContract.CommonDataKinds.Email.TYPE, emailContactType)
             .build()
     )
 }

 // Asking the Contact provider to create a new contact
 try {
     ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops)
 } catch (e: Exception) {
     e.printStackTrace()
 }
}

private fun addContactToContentProvider(rowContactId: String) {

 val addContactsUri = ContactsContract.Data.CONTENT_URI
 // Add an empty contact and get the generated id.
 rowContactId.let {
     contact.name?.let {
         addContactToContentProvider.insertContactDisplayName(
             addContactsUri,
             rowContactId.toLong(),
             it
         )
     }
     addContactToContentProvider.insertContactPhoneNumber(
         addContactsUri,
         rowContactId.toLong(),
         phoneNumbersList
     )
     addContactToContentProvider.insertContactEmail(
         addContactsUri,
         rowContactId.toLong(),
         emailList
     )
 }
}

*/


}
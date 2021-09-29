package com.example.contacts.repositories

import androidx.lifecycle.LiveData
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.database.ContactsDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddContactRepository(private val DBKEY: ContactsDAO) {

    fun getEmail(contactId: String): LiveData<List<EmailDetails>> {
        return DBKEY.getEmailList(contactId)
    }

    fun getPhoneNumbers(contactId: String): LiveData<List<PhoneDetails>> {
        return DBKEY.getPhoneNumbersList(contactId)
    }

    fun addContact(
        contacts: Contacts,
        phoneNumberList: List<PhoneDetails>,
        emailList: List<EmailDetails>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            DBKEY.insertContact(contacts)

            for (phoneDetails in phoneNumberList) {
                DBKEY.insertPhone(phoneDetails)
            }
            for (emailDetails in emailList) {
                DBKEY.insertEmail(emailDetails)
            }
        }
    }

    fun editContact(
        contacts: Contacts,
        phoneNumberList: List<PhoneDetails>,
        emailList: List<EmailDetails>
    ) {

        CoroutineScope(Dispatchers.IO).launch {

            DBKEY.updateContact(contacts.userId, contacts.name, contacts.image)
            DBKEY.deletePhone(contacts.userId)
            DBKEY.deleteEmail(contacts.userId)

            for(i in phoneNumberList) {
                DBKEY.insertPhone(i)
            }
            for (i in emailList) {
                DBKEY.insertEmail(i)
            }
        }
    }

}

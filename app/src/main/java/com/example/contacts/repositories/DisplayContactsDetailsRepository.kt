package com.example.contacts.repositories

import androidx.lifecycle.LiveData
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.database.ContactsDAO

class DisplayContactsDetailsRepository(private val DBKEY: ContactsDAO) {

    fun getPhoneNumbersList(contatID: String): LiveData<List<PhoneDetails>> {

        return DBKEY.getPhoneNumbersList(contatID)
    }

    fun getEmailList(contatID: String): LiveData<List<EmailDetails>> {

        return DBKEY.getEmailList(contatID)
    }
}

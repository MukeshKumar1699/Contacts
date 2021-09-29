package com.example.contacts.repositories

import androidx.lifecycle.LiveData
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.database.ContactsDAO

class SearchContactRepository(DBKEY: ContactsDAO) {

    val ContactList: LiveData<List<Contacts>> = DBKEY.getAllContacts()
    val PhoneList: LiveData<List<PhoneDetails>> = DBKEY.getAllPhoneNumbersList()
    val EmailList: LiveData<List<EmailDetails>> = DBKEY.getAllEmailList()

}

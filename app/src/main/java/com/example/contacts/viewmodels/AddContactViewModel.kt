package com.example.contacts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.repositories.AddContactRepository

class AddContactViewModel(private val repository: AddContactRepository) : ViewModel() {

    fun getEmailIDs(contactId: String): LiveData<List<EmailDetails>> {
        return repository.getEmail(contactId)
    }

    fun getPhoneNumbers(contactId: String): LiveData<List<PhoneDetails>> {
        return repository.getPhoneNumbers(contactId)
    }

    fun addContact(
        contacts: Contacts,
        phoneNumberList: List<PhoneDetails>,
        emailList: List<EmailDetails>
    ) {
        repository.addContact(contacts, phoneNumberList, emailList)
    }

    fun editContact(
        contacts: Contacts,
        phoneNumberList: List<PhoneDetails>,
        emailList: List<EmailDetails>
    ) {
        repository.editContact(contacts, phoneNumberList, emailList)
    }

    override fun onCleared() {
        super.onCleared()
    }

}

class AddContactViewModelFactory(private val repository: AddContactRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddContactViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
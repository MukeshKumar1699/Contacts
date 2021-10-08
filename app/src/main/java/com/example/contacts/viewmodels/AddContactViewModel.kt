package com.example.contacts.viewmodels

import androidx.lifecycle.*
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.repositories.AddContactRepository
import kotlinx.coroutines.launch

class AddContactViewModel(private val repository: AddContactRepository) : ViewModel() {

    private var emailMLiveData = MutableLiveData<List<EmailDetails>>()
    private var phoneMLiveData = MutableLiveData<List<PhoneDetails>>()


    fun fetchEmailID(contactId: String) {
        emailMLiveData = repository.getEmail(contactId) as MutableLiveData<List<EmailDetails>>

    }

    fun fetchPhoneNumber(contactId: String) {
            phoneMLiveData = repository.getPhoneNumbers(contactId) as MutableLiveData<List<PhoneDetails>>

    }

    fun sample(): Int{
        return 2
    }

    fun getEmailIDs(): LiveData<List<EmailDetails>> {
        return emailMLiveData
    }

    fun getPhoneNumbers(): LiveData<List<PhoneDetails>> {
        return phoneMLiveData
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
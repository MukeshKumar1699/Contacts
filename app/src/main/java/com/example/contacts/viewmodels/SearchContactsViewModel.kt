package com.example.contacts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.repositories.SearchContactRepository

class SearchContactsViewModel(private val repository: SearchContactRepository) : ViewModel() {

    var contactLiveData: LiveData<List<Contacts>> = repository.ContactList
    var phoneLiveData: LiveData<List<PhoneDetails>> = repository.PhoneList
    var emailLiveData: LiveData<List<EmailDetails>> = repository.EmailList


    companion object {

        private const val TAG = "SearchContactsViewModel"
    }

}

class SearchContactViewModelFactory(private val repository: SearchContactRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchContactsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
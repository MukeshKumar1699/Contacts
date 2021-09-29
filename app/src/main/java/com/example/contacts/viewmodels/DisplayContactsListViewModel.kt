package com.example.contacts.viewmodels

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.contacts.Contacts
import com.example.contacts.repositories.DisplayContactsListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DisplayContactsListViewModel(private val repository: DisplayContactsListRepository) :
    ViewModel() {


    fun getContacts(contentResolver: ContentResolver) = viewModelScope.launch {

        launch { repository.getAllContactsDetailsFromContentProvider(contentResolver) }
        launch { repository.getAllPhoneNumbersFromContentProvider(contentResolver) }
        launch { repository.getAllEmailDetailsFromContentProvider(contentResolver) }
    }

    var contactsLiveData: LiveData<List<Contacts>> = repository.ContactList



    fun deleteContactFromDB(contact: Contacts) {

        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteContact(contact)
        }
    }

}

class DisplayContactListViewModelFactory(private val repository: DisplayContactsListRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayContactsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DisplayContactsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
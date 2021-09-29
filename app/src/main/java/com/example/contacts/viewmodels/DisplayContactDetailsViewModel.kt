package com.example.contacts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.repositories.DisplayContactsDetailsRepository
import kotlinx.coroutines.launch

class DisplayContactDetailsViewModel(private val repository: DisplayContactsDetailsRepository) :
    ViewModel() {

    lateinit var phoneLiveData: LiveData<List<PhoneDetails>>
    lateinit var emailLiveData: LiveData<List<EmailDetails>>

    fun getPhoneNumbersList(contactID: String) = viewModelScope.launch {
        phoneLiveData = repository.getPhoneNumbersList(contactID)
    }

    fun getEmailList(contactID: String) = viewModelScope.launch {
        emailLiveData = repository.getEmailList(contactID)
    }

}

class DisplayContactDetailsViewModelFactory(private val repository: DisplayContactsDetailsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayContactDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DisplayContactDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.contacts

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DisplayContactsListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DisplayContactsListRepository()

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext


    private lateinit var contactList: List<Contacts>
    private var mutableLiveData = MutableLiveData<List<Contacts>>()
    val liveData: LiveData<List<Contacts>> = mutableLiveData

    fun getContacts(contentResolver: ContentResolver) {

        contactList = repository.getContacts(contentResolver, context)
        mutableLiveData.value = contactList
    }

    companion object {
        private val TAG = "DisplayContactsListView"

    }

}
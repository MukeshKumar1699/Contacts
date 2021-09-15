package com.example.contacts

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayContactsListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DisplayContactsListRepository()

    private val scope = CoroutineScope(Dispatchers.IO)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val DBKEY = AppDatabase.getInstance(context).contactsDAO

    private lateinit var contactList: List<Contacts>
    private var mutableLiveData = MutableLiveData<List<Contacts>>()
    val liveData: LiveData<List<Contacts>> = mutableLiveData

    init {

        scope.launch {

            contactList = DBKEY.getContacts()
            withContext(Dispatchers.Main) {
                mutableLiveData.value = contactList
            }
        }
    }

    fun getContacts(contentResolver: ContentResolver) {

        scope.launch {
            repository.getAllContactsDetails(contentResolver, DBKEY)
            repository.getAllPhoneNumbers(contentResolver, DBKEY)
            repository.getAllEmailDetails(contentResolver, DBKEY)

        }
    }

    suspend fun searchContact(contentResolver: ContentResolver, name: String): List<Contacts> {

        val task1 = scope.launch {
            contactList = DBKEY.searchContactByName(name)
        }
        task1.join()
        return contactList

    }

    companion object {
        private val TAG = "DisplayContactsListView"

    }

}
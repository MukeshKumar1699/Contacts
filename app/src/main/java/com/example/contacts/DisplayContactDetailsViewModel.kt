package com.example.contacts

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DisplayContactDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var phoneDetailsList: List<PhoneDetails>

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val DBKEY = AppDatabase.getInstance(context).contactsDAO

    private val mutableLiveData = MutableLiveData<List<PhoneDetails>>()
    val phoneDetailsLiveData = mutableLiveData

    fun getPhoneNumbersList(contactID: String) {
        CoroutineScope(Dispatchers.IO).launch {

            phoneDetailsList = DBKEY.getPhoneNumbersList(contactID)

            withContext(Dispatchers.Main) {
                mutableLiveData.value = phoneDetailsList
            }
        }
    }

}
package com.example.contacts.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.Query
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails


@androidx.room.Dao
interface ContactsDAO {

    @Insert
    @Ignore
    suspend fun insertContact(contact: Contacts)

    @Insert
    @Ignore
    suspend fun insertPhone(phone: PhoneDetails)

    @Insert
    @Ignore
    suspend fun insertEmail(email: EmailDetails)

    @Query("SELECT * FROM CONTACTS_DB")
    fun getAllContacts(): LiveData<List<Contacts>>

    @Query("SELECT * FROM PHONE_DB")
    fun getAllPhoneNumbersList(): LiveData<List<PhoneDetails>>

    @Query("SELECT * FROM EMAIL_DB")
    fun getAllEmailList(): LiveData<List<EmailDetails>>

    @Query("SELECT * FROM PHONE_DB WHERE ContactID= :contactID")
    fun getPhoneNumbersList(contactID: String): LiveData<List<PhoneDetails>>

    @Query("SELECT * FROM EMAIL_DB WHERE ContactID= :contactID")
    fun getEmailList(contactID: String): LiveData<List<EmailDetails>>

    @Delete()
    fun detelteContact(contact: Contacts)

    @Query("DELETE From PHONE_DB WHERE ContactID= :contactID")
    fun deletePhone(contactID: String)

    @Query("DELETE From EMAIL_DB WHERE ContactID= :contactID")
    fun deleteEmail(contactID: String)

    @Query("UPDATE CONTACTS_DB SET ContactName=:name, ContactImage= :image WHERE ContactID= :contactID")
    fun updateContact(contactID: String, name: String?, image: String?)

//    @Query("UPDATE PHONE_DB SET Number= :number, Type= :type WHERE SNo= :sNo")
//    fun updateContactPhoneNumber(number: String, type: String, sNo: Int)
//
//    @Query("UPDATE EMAIL_DB set MailID= :email, Type= :type WHERE SNo= :sNo")
//    fun updateContactEmail(email: String, type: String, sNo: Int)
}
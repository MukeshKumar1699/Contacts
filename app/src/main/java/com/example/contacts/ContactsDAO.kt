package com.example.contacts

import androidx.room.Insert
import androidx.room.Query

@androidx.room.Dao
interface ContactsDAO {

    @Insert
    suspend fun insertContact(contacts: Contacts)

    @Insert
    suspend fun insertPhone(phone:PhoneDetails)

    @Insert
    suspend fun insertEmail(email: EmailDetails)

    @Query("SELECT * FROM CONTACTS_DB")
    fun getContacts(): List<Contacts>

    @Query("SELECT * FROM PHONE_DB WHERE ContactID== :contactID")
    fun getPhoneNumbersList(contactID: String): List<PhoneDetails>

    @Query("SELECT * FROM CONTACTS_DB WHERE ContactName== :name")
    fun searchContactByName(name: String): List<Contacts>

//    @Insert
//    suspend fun syncWithContectProvider(contactsList: List<Contacts>)
//
//    @Update
//    fun update(contacts: Contacts)
//
//    @Delete
//    fun delete(contacts: Contacts)
//
//
//
//    @Query("SELECT * FROM CONTACTS WHERE lastName== :name")
//    fun searchContactByLastName(name: String): List<Contacts>
}
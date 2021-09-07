package com.example.contacts

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@androidx.room.Dao
interface ContactsDAO {

    @Insert
    suspend fun insert(contacts: Contacts)

//    @Insert
//    suspend fun syncWithContectProvider(contactsList: List<Contacts>)
//
//    @Update
//    fun update(contacts: Contacts)
//
//    @Delete
//    fun delete(contacts: Contacts)

//    @Query("SELECT * FROM CONTACTS")
//    fun getContacts(): LiveData<List<Contacts>>

//    @Query("SELECT * FROM CONTACTS WHERE firstName== :name")
//    fun searchContactByFirstName(name: String): List<Contacts>
//
//    @Query("SELECT * FROM CONTACTS WHERE lastName== :name")
//    fun searchContactByLastName(name: String): List<Contacts>
}
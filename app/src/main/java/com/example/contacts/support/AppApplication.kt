package com.example.contacts.support

import android.app.Application
import com.example.contacts.database.AppDatabase
import com.example.contacts.repositories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AppApplication : Application() {

    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getInstance(this, applicationScope) }
    val displayContactsListRepository by lazy { DisplayContactsListRepository(database.contactsDAO()) }
    val searchContactRepository by lazy { SearchContactRepository(database.contactsDAO()) }
    val displayContactsDetailsRepository by lazy { DisplayContactsDetailsRepository(database.contactsDAO()) }
    val addContactRepository by lazy { AddContactRepository(database.contactsDAO()) }
}
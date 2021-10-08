package com.example.contacts.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Contacts::class, PhoneDetails::class, EmailDetails::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactsDAO(): ContactsDAO


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, applicationScope: CoroutineScope): AppDatabase {

            var instance = INSTANCE
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, AppDatabase::class.java, "CONTACTS_DB")
                        .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}
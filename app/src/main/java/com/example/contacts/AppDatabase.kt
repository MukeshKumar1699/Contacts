package com.example.contacts

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Contacts::class, PhoneDetails::class, EmailDetails::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract val contactsDAO: ContactsDAO

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

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
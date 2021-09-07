package com.example.contacts

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class AppDatabase : RoomDatabase() {

    abstract val contactsDAO: ContactsDAO

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {

            var instance = INSTANCE
            if (instance == null) {
                instance =
                    Room.databaseBuilder(context, AppDatabase::class.java, "CONTACTS")
                        .build()
                INSTANCE = instance
            }
            return instance
        }
    }
}
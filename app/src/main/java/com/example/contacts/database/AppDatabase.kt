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

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.contactsDAO()

//                    // Delete all content here.
//                    wordDao.deleteAll()
//
//                    // Add sample words.
//                    var word = Word("Hello")
//                    wordDao.insert(word)
//                    word = Word("World!")
//                    wordDao.insert(word)
//
//                    // TODO: Add your own words!
//                    word = Word("TODO!")
//                    wordDao.insert(word)
                }
            }
        }
    }


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
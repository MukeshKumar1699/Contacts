package com.example.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DisplayContactsListRepository {

    val contactList: ArrayList<Contacts> = ArrayList()


    @SuppressLint("Range", "Recycle")
    fun getContacts(cr: ContentResolver, context: Context): ArrayList<Contacts> {

        CoroutineScope(Dispatchers.IO).launch {

            val contacts = Contacts()

            val cur = cr.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, SORT_ORDER
            )

            if (cur != null && cur.count > 0) {

                while (cur.moveToNext()) {

                    contacts.userId =
                        cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID)).toInt()
                    contacts.name =
                        cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    contacts.image =
                        cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                    // Create query to use CommonDataKinds classes to fetch emails
                    val eCur = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        null, null, null
                    )

                    if (eCur != null) {
                        while (eCur.moveToNext()) {

                            val emailDetails = EmailDetails()

//                            emailDetails.label = eCur.getString(
//                                eCur.getColumnIndex(
//                                    ContactsContract.CommonDataKinds.Email.LABEL
//                                )
//                            )
                            emailDetails.mail = eCur.getString(
                                eCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Email.DATA
                                )
                            )

                            contacts.emailList.add(emailDetails)
                        }
                        eCur.close()

                        for (i in contacts.emailList) {
                            Log.d(TAG, "emailList: ${i.mail}")
                        }
                    }


                    if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        val pCur: Cursor = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            null,
                            SORT_ORDER
                        )!!

                        while (pCur.moveToNext()) {

                            val phoneDetails = PhoneDetails()

                            phoneDetails.label = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.TYPE
                                )
                            )
                            phoneDetails.number = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            contacts.phoneList.add(phoneDetails)
                        }
                        pCur.close()
                        Log.d(TAG, "getContacts: ${contacts.phoneList}")

                    }
//                    AppDatabase.getInstance(context).contactsDAO.insert(contacts)
                    contactList.add(contacts)
                }
            }
        }

        return contactList
    }


    companion object {

        private const val TAG = "DisplayContactsListRepo"
        private const val SORT_ORDER = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

    }
}





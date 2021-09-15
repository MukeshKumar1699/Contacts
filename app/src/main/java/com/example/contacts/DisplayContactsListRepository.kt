package com.example.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log


class DisplayContactsListRepository {

    @SuppressLint("Range", "Recycle")
    suspend fun getAllPhoneNumbers(cr: ContentResolver, DBKEY: ContactsDAO) {

        var number = ""
        var label = ""
        var type: Int

        val phoneCUR: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, SORT_ORDER
        )
        if (phoneCUR != null) {

            while (phoneCUR.moveToNext()) {

                val contactID =
                    phoneCUR.getString(phoneCUR.getColumnIndex(Phone.CONTACT_ID))

                val phone: Cursor? = cr.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    SORT_ORDER
                )

                if (phone!!.moveToFirst()) {

                    number =
                        phoneCUR.getString(phoneCUR.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    type =
                        phoneCUR.getInt(phoneCUR.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))

                    Log.d("before switch", "contactId $contactID number $number")

                    when (type) {
                        ContactsContract.CommonDataKinds.Email.TYPE_HOME -> label = "HOME"
                        ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> label = "MOBILE"
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK -> label = "WORK"
                        ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM -> label = "CUSTOM"
                        else -> {
                        }
                    }

                    Log.d(TAG, "getContactsWith: $label")
                    val phoneDetails = PhoneDetails(number, type = label, contactID)
                    DBKEY.insertPhone(phoneDetails)
                }
//                phones.close()
            }
        }

    }

    @SuppressLint("Range", "Recycle")
    suspend fun getAllContactsDetails(cr: ContentResolver, DBKEY: ContactsDAO) {

        val cur = cr.query(
            ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER
        )

        if (cur != null && cur.moveToFirst()) {

            do {
                val userid: String =
                    cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String =
                    cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val image: String = getPhotoUri(cr, userid.toLong()).toString()

                val contacts = Contacts(userId = userid, name = name, image = image)

                DBKEY.insertContact(contacts)
            } while (cur.moveToNext())
        }
    }

    @SuppressLint("Recycle")
    fun getPhotoUri(contentResolver: ContentResolver, contactId: Long): Uri? {

        try {
            val cursor = contentResolver
                .query(
                    ContactsContract.Data.CONTENT_URI,
                    null,
                    ContactsContract.Data.CONTACT_ID
                            + "="
                            + contactId
                            + " AND "
                            + ContactsContract.Data.MIMETYPE
                            + "='"
                            + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                            + "'", null, null
                )
            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    return null // no photo
                }
            } else {
                return null // error in cursor process
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        val person: Uri = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI, contactId
        )
        return Uri.withAppendedPath(
            person,
            ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
        )
    }

    @SuppressLint("Range", "Recycle")
    suspend fun getAllEmailDetails(cr: ContentResolver, DBKEY: ContactsDAO) {

        var email = ""
        var labe = ""
        var type: Int

        val eMail: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null, SORT_ORDER
        )
        if (eMail != null) {

            while (eMail.moveToNext()) {

                val contactID =
                    eMail.getString(eMail.getColumnIndex(Phone.CONTACT_ID))

                val phone1: Cursor? = cr.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    SORT_ORDER
                )

                if (phone1!!.moveToFirst()) {


                    email =
                        eMail.getString(eMail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    type =
                        eMail.getInt(eMail.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))

                    Log.d("before switch", "contactId $contactID number $email")

                    when (type) {
                        ContactsContract.CommonDataKinds.Email.TYPE_HOME -> labe = "HOME"
                        ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> labe = "MOBILE"
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK -> labe = "WORK"
                        ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM -> labe = "CUSTOM"
                        else -> {
                        }
                    }

                    Log.d(TAG, "getContactsWith: $labe")
                    val emailDetails = EmailDetails(email, type = labe, contactID)
                    DBKEY.insertEmail(emailDetails)
                }
//                phones.close()
            }
        }

    }

    companion object {

        private const val TAG = "DisplayContactsListRepo"
        private const val SORT_ORDER = "${Phone.DISPLAY_NAME} ASC"

        @SuppressLint("ObsoleteSdkInt")
        private val DISPLAY_NAME =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ContactsContract.Contacts.DISPLAY_NAME_PRIMARY else ContactsContract.Contacts.DISPLAY_NAME
        private val PROJECTION = arrayOf(
            ContactsContract.Contacts._ID,
            DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )

        private val FILTER = "$DISPLAY_NAME NOT LIKE '%@%'"

        private val ORDER = String.format("%1\$s COLLATE NOCASE", DISPLAY_NAME)
    }
}

package com.example.contacts.repositories

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.contacts.Contacts
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails
import com.example.contacts.database.ContactsDAO
import java.util.*
import kotlin.collections.ArrayList


class DisplayContactsListRepository(private val DBKEY: ContactsDAO) {

    val ContactList: LiveData<List<Contacts>> = DBKEY.getAllContacts()

    @SuppressLint("Range", "Recycle")
    suspend fun getAllPhoneNumbersFromContentProvider(cr: ContentResolver) {

        var number = ""
        var label = ""
        var type: Int

        val phoneCUR: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
            SORT_ORDER
        )
        if (phoneCUR != null) {

            while (phoneCUR.moveToNext()) {

                val contactID =
                    phoneCUR.getString(phoneCUR.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))

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

                    when (type) {
                        ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> label = "HOME"
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> label = "MOBILE"
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> label = "WORK"
                        ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM -> label = "CUSTOM"
                        else -> {
                            label = "OTHER"
                        }
                    }
                    val phoneDetails = PhoneDetails(number, type = label, contactID)
                    addPhoneNumberTODB(phoneDetails)
                }
            }
            phoneCUR.close()
        }
    }

    @SuppressLint("Range", "Recycle")
    suspend fun getAllContactsDetailsFromContentProvider(cr: ContentResolver) {

        val contactsList: List<Contacts> = ArrayList()

        val cur = cr.query(
            ContactsContract.Contacts.CONTENT_URI, null, null, null,
            SORT_ORDER
        )

        if (cur != null && cur.moveToFirst()) {

            while (cur.moveToNext()) {
                val userid: String =
                    cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String? =
                    cur.getString(cur.getColumnIndex(DISPLAY_NAME))
                val image: String = getPhotoUri(cr, userid.toLong()).toString()

                name?.let {
                    val contacts = Contacts(userId = userid, name = name, image = image)
                    contacts.color = createColor().toString()
                    contactsList.plus(contacts)
                    addContactTODB(contacts)
                }

            }
            cur.close()
        }

    }

    @SuppressLint("Range", "Recycle")
    suspend fun getAllEmailDetailsFromContentProvider(cr: ContentResolver) {

        var email = ""
        var labe = ""
        var type: Int

        val eMail: Cursor? = cr.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null,
            SORT_ORDER
        )
        if (eMail != null) {

            while (eMail.moveToNext()) {

                val contactID =
                    eMail.getString(eMail.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))

                val eCur: Cursor? = cr.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    SORT_ORDER
                )

                if (eCur!!.moveToFirst()) {

                    email =
                        eMail.getString(eMail.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                    type =
                        eMail.getInt(eMail.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE))

//                    Log.d("before switch", "contactId $contactID number $email")

                    when (type) {
                        ContactsContract.CommonDataKinds.Email.TYPE_HOME -> labe = "HOME"
                        ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> labe = "MOBILE"
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK -> labe = "WORK"
                        ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM -> labe = "CUSTOM"
                        else -> {
                        }
                    }
                    val emailDetails = EmailDetails(email, type = labe, contactID)
                    addEmailTODB(emailDetails)
                }
                eCur.close()
            }
        }

    }

    fun createColor(): Int {

        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        return color
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

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addContactTODB(contact: Contacts) {
        DBKEY.insertContact(contact)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addPhoneNumberTODB(phone: PhoneDetails) {
        DBKEY.insertPhone(phone)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun addEmailTODB(email: EmailDetails) {
        DBKEY.insertEmail(email)
    }

    fun deleteContact(contacts: Contacts) {
        DBKEY.detelteContact(contacts)
        DBKEY.deletePhone(contacts.userId)
        DBKEY.deleteEmail(contacts.userId)

    }

    companion object {
        private const val SORT_ORDER = "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"

        @SuppressLint("ObsoleteSdkInt")
        private val DISPLAY_NAME =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ContactsContract.Contacts.DISPLAY_NAME_PRIMARY else ContactsContract.Contacts.DISPLAY_NAME

    }
}
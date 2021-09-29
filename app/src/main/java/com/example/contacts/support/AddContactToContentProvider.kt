package com.example.contacts.support

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.provider.ContactsContract
import com.example.contacts.EmailDetails
import com.example.contacts.PhoneDetails

class AddContactToContentProvider(val contentResolver: ContentResolver) {

    fun getRawContactId(): Long {

        // Insert an empty contact.
        val contentValues = ContentValues()
        val rawContactUri: Uri =
            contentResolver.insert(
                ContactsContract.RawContacts.CONTENT_URI,
                contentValues
            )!!
        // Get the newly created contact raw id.
        return ContentUris.parseId(rawContactUri)
    }

    fun insertContactDisplayName(
        addContactsUri: Uri,
        rawContactId: Long,
        displayName: String
    ) {
        val contentValues = ContentValues()
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
        // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
        contentValues.put(
            ContactsContract.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
        )
        // Put contact display name value.
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, displayName)
        contentResolver.insert(addContactsUri, contentValues)
    }

    fun insertContactPhoneNumber(
        addContactsUri: Uri,
        rawContactId: Long,
        phoneNumberList: List<PhoneDetails>
    ) {
        for (i in phoneNumberList) {

            // Create a ContentValues object.
            val contentValues = ContentValues()
            // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
            contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
            contentValues.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            )

            // Put phone number value.
            contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, i.number)
            // Calculate phone type by user selection.
            var phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER

            when (i.type) {
                "HOME" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                "MOBILE" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                "WORK" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                "CUSTOM" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM
                "OTHER" -> phoneContactType = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
            }

            // Put phone type value.
            contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, phoneContactType)
            // Insert new contact data into phone contact list.
            contentResolver.insert(addContactsUri, contentValues)
        }

    }

    fun insertContactEmail(
        addContactsUri: Uri,
        rawContactId: Long,
        emailList: List<EmailDetails>
    ) {
        for (i in emailList) {

            // Create a ContentValues object.
            val contentValues = ContentValues()
            // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
            contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId)
            // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
            contentValues.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
            )

            // Put phone number value.
            contentValues.put(ContactsContract.CommonDataKinds.Email.DATA, i.mailID)

            var emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_OTHER

            when (i.type) {
                "HOME" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_HOME
                "MOBILE" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
                "WORK" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_WORK
                "CUSTOM" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_CUSTOM
                "OTHER" -> emailContactType = ContactsContract.CommonDataKinds.Email.TYPE_OTHER
            }

            // Put phone type value.
            contentValues.put(ContactsContract.CommonDataKinds.Phone.TYPE, emailContactType)
            // Insert new contact data into phone contact list.
            contentResolver.insert(addContactsUri, contentValues)
        }
    }
}
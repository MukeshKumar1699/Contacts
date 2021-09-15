package com.example.contacts

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "CONTACTS_DB")
data class Contacts(

    @ColumnInfo(name = "ContactId")
    var userId: String,

    @ColumnInfo(name = "ContactName")
    var name: String?,

    @ColumnInfo(name = "ContactImage")
    var image: String?,

    ) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sNo")
    var sNo: Int = 0
}


@Entity(tableName = "PHONE_DB")
data class PhoneDetails(

    @ColumnInfo(name = "Number")
    var number: String = "",

    @ColumnInfo(name = "Type")
    var type: String = "",

    @ColumnInfo(name = "ContactID")
    var contactId: String

) {
    @PrimaryKey(autoGenerate = true)
    var sNo: Int = 0
}

@Entity(tableName = "EMAIL_DB")
data class EmailDetails(

    @ColumnInfo(name = "MailID")
    var mailID: String = "",

    @ColumnInfo(name = "Type")
    var type: String = "",

    @ColumnInfo(name = "ContactID")
    var contactId: String

) {
    @PrimaryKey(autoGenerate = true)
    var sNo: Int = 0
}

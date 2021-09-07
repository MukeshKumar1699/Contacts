package com.example.contacts

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "CONTACTS")
data class Contacts (

    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,

    var name: String? = null,

    @ColumnInfo(name = "contactImage")
    var image: String? = null,

    @ColumnInfo(name = "phoneList")
    var phoneList: ArrayList<PhoneDetails> = arrayListOf(),

    @ColumnInfo(name = "emailList")
    var emailList: ArrayList<EmailDetails> = arrayListOf()
)

//    @ColumnInfo(name = "company")
//    val company: String = ""
//
//    @ColumnInfo(name = "department")
//    val department: String = ""
//
//    @ColumnInfo(name = "title")
//    val title: String = ""

//    @ColumnInfo(name = "addressList")
//    lateinit var addressList: ArrayList<AddressDetails>
//
//    @ColumnInfo(name = "website")
//    val website: String = ""
//
//    @ColumnInfo(name = "significantDateList")
//    lateinit var significantDateList: ArrayList<SignificantDate>
//
//    @ColumnInfo(name = "RelationShipList")
//    lateinit var RelationShipList: ArrayList<RelationShip>

//}

class EmailDetails {

    var mail: String = ""
    var label: String = ""
}

class PhoneDetails {

    var number: String = ""
    var label: String = ""

}

class AddressDetails {

    val address: String = ""
    val label: String = ""

}

class SignificantDate {

    val date: String = ""
    val label: String = ""

}

class RelationShip {

    val relation: String = ""
    val label: String = ""

}

class Custom {

    val custom: String = ""
    val label: String = ""

}

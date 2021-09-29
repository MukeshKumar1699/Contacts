package com.example.contacts.support

import com.example.contacts.Contacts


fun main() {
    val smallList: ArrayList<Contacts> = arrayListOf()


    for (i in 0..10) {
        val contacts = Contacts("$i", "$i", "$i")
        smallList.add(contacts)
    }
    upDateAdapter(smallList)
    println("upDateAdapter: SmallList ${smallList.size}")

}

fun upDateAdapter(smallList: ArrayList<Contacts>) {

    val locList = smallList
    val contacts = Contacts("mukesh", "mnukesh", "mukesh")

    locList.add(contacts)
    locList.clear()

    println("upDateAdapter: LocalList${locList.size}")
}

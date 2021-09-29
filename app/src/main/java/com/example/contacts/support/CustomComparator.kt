package com.example.contacts.support

import com.example.contacts.Contacts

class CustomComparator : Comparator<Contacts> {
    override fun compare(o1: Contacts, o2: Contacts): Int {
        return o2.name?.let { o1.name?.compareTo(it) }!!
    }


}
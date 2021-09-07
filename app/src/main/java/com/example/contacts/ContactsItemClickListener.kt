package com.example.contacts

import java.text.FieldPosition

interface ContactsItemClickListener {

    fun onItemClicked(position: Int, contacts: Contacts)
}
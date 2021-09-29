package com.example.contacts.listeners

import com.example.contacts.Contacts

interface ContactsItemClickListener {

    fun onItemClicked(position: Int, contacts: Contacts)
}
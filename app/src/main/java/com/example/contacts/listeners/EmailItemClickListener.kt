package com.example.contacts.listeners

import com.example.contacts.EmailDetails

interface EmailItemClickListener {

    fun onItemClicked(position: Int, emailDetails: EmailDetails)


}
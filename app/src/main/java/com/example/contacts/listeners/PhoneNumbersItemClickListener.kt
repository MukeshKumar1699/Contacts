package com.example.contacts.listeners

import com.example.contacts.PhoneDetails

interface PhoneNumbersItemClickListener {

    fun onItemClicked(position: Int, phoneDetails: PhoneDetails, action: Int)


}
package com.alexP.socialnetwork.presentation.main.contacts.adapter

import android.widget.ImageView
import com.alexp.contactsprovider.Contact

interface IContactActionListener {
    fun onContactDelete(contact: Contact)
    fun onContactDetails(contact: Contact, imageView: ImageView)
    fun onContactSelect(contact: Contact)
}
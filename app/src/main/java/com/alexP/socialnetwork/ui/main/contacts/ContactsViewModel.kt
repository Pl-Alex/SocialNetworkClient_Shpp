package com.alexP.socialnetwork.ui.main.contacts

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alexp.contactsprovider.Contact
import com.alexp.contactsprovider.ContactsListener
import com.alexp.contactsprovider.ContactsProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val contactsServices: ContactsProvider,
) : ViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts

    private val deletedContacts = mutableListOf<Contact>()

    private var cleanDeletedContactsJob: Job? = null

    private val listener: ContactsListener = {
        _contacts.value = it
    }

    init {
        loadContacts()
    }

    override fun onCleared() {
        super.onCleared()
        contactsServices.removeListener(listener)
    }

    private fun loadContacts() {
        contactsServices.addListener(listener)
    }

    fun recoverContacts() {
        deletedContacts.forEach {
            contactsServices.addContact(it)
        }
        deletedContacts.clear()
    }

    fun addContact(contact: Contact) {
        contactsServices.addContact(contact.copy(id = contactsServices.getNewId()))
    }

    fun addContacts(contentResolver: ContentResolver) {
        val contacts = contactsServices.fetchContacts(contentResolver)
        for (contact in contacts) {
            contactsServices.addContact(contact.copy(id = contactsServices.getNewId()))
        }
    }

    fun deleteContact(contact: Contact) {
        if (deletedContacts.contains(contact)) return
        deletedContacts.add(contact)
        contactsServices.deleteContact(contact)

        cleanDeletedContactsJob = viewModelScope.launch {
            delay(5000)
            deletedContacts.remove(contact)
        }
    }

    fun deleteContacts(contacts: List<Contact>) {
        contacts.forEach { deleteContact(it) }
    }

    companion object {
        fun createFactory(contactsServices: ContactsProvider): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ContactsViewModel(contactsServices)
                }
            }
    }
}
package com.alexP.socialnetwork.ui.main.contactsdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactsDetailsViewModel : ViewModel() {
    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> get() = _fullName

    private val _career = MutableLiveData<String>()
    val career: LiveData<String> get() = _career

    private val _homeAddress = MutableLiveData<String>()
    val homeAddress: LiveData<String> get() = _homeAddress

    private val _photo = MutableLiveData<String>()
    val photo: LiveData<String> get() = _photo

    fun setContactDetails(fullName: String, career: String, homeAddress: String, photo: String) {
        _fullName.value = fullName
        _career.value = career
        _homeAddress.value = homeAddress
        _photo.value = photo
    }
}
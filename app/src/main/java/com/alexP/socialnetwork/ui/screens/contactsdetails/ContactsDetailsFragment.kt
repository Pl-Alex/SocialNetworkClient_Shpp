package com.alexP.socialnetwork.ui.screens.contactsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.alexP.socialnetwork.databinding.FragmentContactDetailsBinding
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.ui.screens.contacts.ContactsFragment.Companion.CAREER
import com.alexP.socialnetwork.ui.screens.contacts.ContactsFragment.Companion.FULL_NAME
import com.alexP.socialnetwork.ui.screens.contacts.ContactsFragment.Companion.HOME_ADDRESS
import com.alexP.socialnetwork.ui.screens.contacts.ContactsFragment.Companion.PHOTO
import com.alexP.socialnetwork.utils.applyWindowInsets
import com.alexP.socialnetwork.utils.loadCircularImage

class ContactsDetailsFragment : BaseFragment<FragmentContactDetailsBinding>(){

    private val vm: ContactsDetailsViewModel by viewModels()

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentContactDetailsBinding {
        return FragmentContactDetailsBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.applyWindowInsets()
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        arguments?.let {
            vm.setContactDetails(
                it.getString(FULL_NAME, ""),
                it.getString(CAREER, ""),
                it.getString(HOME_ADDRESS, ""),
                it.getString(PHOTO, "")
            )
        }

        binding.topBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeViewModel() {
        vm.fullName.observe(viewLifecycleOwner) {
            binding.textViewNameSurname.text = it
        }
        vm.career.observe(viewLifecycleOwner) {
            binding.textViewCareer.text = it
        }
        vm.homeAddress.observe(viewLifecycleOwner) {
            binding.textViewHomeAddress.text = it
        }
        vm.photo.observe(viewLifecycleOwner) {
            binding.imageViewProfileImage.loadCircularImage(it)
        }
    }


}
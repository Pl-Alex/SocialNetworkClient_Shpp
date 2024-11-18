package com.alexP.socialnetwork.ui.screens.contactsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexP.socialnetwork.databinding.FragmentContactDetailsBinding
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.ui.screens.contacts.ContactsFragment.Companion.CAREER
import com.alexP.socialnetwork.ui.screens.contacts.ContactsFragment.Companion.FULL_NAME
import com.alexP.socialnetwork.ui.screens.contacts.ContactsFragment.Companion.HOME_ADDRESS
import com.alexP.socialnetwork.ui.screens.contacts.ContactsFragment.Companion.PHOTO
import com.alexP.socialnetwork.utils.applyWindowInsets
import com.alexP.socialnetwork.utils.enableTransitionAnimation
import com.alexP.socialnetwork.utils.loadCircularImage
import java.util.concurrent.TimeUnit

class ContactsDetailsFragment : BaseFragment<FragmentContactDetailsBinding>(){

    private val vm: ContactsDetailsViewModel by viewModels()

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentContactDetailsBinding {
        return FragmentContactDetailsBinding.inflate(inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        enableTransitionAnimation()
        postponeEnterTransition(1000, TimeUnit.MILLISECONDS)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.applyWindowInsets()
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
            findNavController().popBackStack()
        }

        binding.root.doOnPreDraw {
            startPostponedEnterTransition()
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
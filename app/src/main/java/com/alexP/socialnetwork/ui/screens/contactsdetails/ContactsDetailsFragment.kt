package com.alexP.socialnetwork.ui.screens.contactsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alexP.socialnetwork.databinding.FragmentContactDetailsBinding
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.utils.applyWindowInsets

class ContactsDetailsFragment : BaseFragment<FragmentContactDetailsBinding>(){

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentContactDetailsBinding {
        return FragmentContactDetailsBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.applyWindowInsets()
        super.onViewCreated(view, savedInstanceState)

        binding.topBar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
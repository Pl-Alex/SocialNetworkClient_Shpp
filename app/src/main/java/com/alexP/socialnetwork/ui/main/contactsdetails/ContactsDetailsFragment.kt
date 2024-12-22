package com.alexP.socialnetwork.ui.main.contactsdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alexP.socialnetwork.databinding.FragmentContactDetailsBinding
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.utils.applyWindowInsets
import com.alexP.socialnetwork.utils.enableTransitionAnimation
import com.alexP.socialnetwork.utils.loadCircularImage
import java.util.concurrent.TimeUnit

class ContactsDetailsFragment : BaseFragment<FragmentContactDetailsBinding>() {

    private val vm: ContactsDetailsViewModel by viewModels()
    private val args: ContactsDetailsFragmentArgs by navArgs()

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

        vm.setContactDetails(
            args.fullName,
            args.career,
            args.address,
            args.photo
        )

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
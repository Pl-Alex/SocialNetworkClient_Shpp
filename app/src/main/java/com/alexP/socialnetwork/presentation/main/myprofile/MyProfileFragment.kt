package com.alexP.socialnetwork.presentation.main.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alexP.socialnetwork.databinding.FragmentMyprofileBinding
import com.alexP.socialnetwork.presentation.base.BaseFragment
import com.alexP.socialnetwork.utils.applyWindowInsets
import com.alexP.socialnetwork.utils.loadCircularImage
import com.alexp.datastore.DataStoreProvider
import kotlinx.coroutines.launch

class MyProfileFragment : BaseFragment<FragmentMyprofileBinding>() {


    private val viewModel: MyProfileViewModel by viewModels{
        MyProfileViewModel.createFactory(DataStoreProvider(requireContext()))
    }
    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMyprofileBinding {
        return FragmentMyprofileBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.applyWindowInsets()
        binding.imageViewProfileImage.loadCircularImage(IMAGE_LINK)

        setListeners()
        lifecycleScope.launch {
            viewModel.myProfileState.collect { state ->
                binding.textViewNameSurname.text = state.username
            }
        }
    }


    private fun setListeners() {
        binding.buttonLogOut.setOnClickListener {
            onLogOutButtonPressed()
        }
    }

    private fun onLogOutButtonPressed() {
        viewModel.cleanStorage()
    }

    companion object {
        const val IMAGE_LINK =
            "https://unsplash.com/photos/_vnKbf9K-Vo/download?ixid=M3wxMjA3fDB8MXxhbGx8MTE4fHx8fHx8Mnx8MTcwMTgxMDA2MHw&force=true&w=640"
    }
}
package com.alexP.socialnetwork.ui.auth.singUpExtended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.alexP.socialnetwork.databinding.FragmentSingUpExtendedBinding
import com.alexP.socialnetwork.ui.base.BaseFragment

class SingUpExtendedFragment : BaseFragment<FragmentSingUpExtendedBinding>() {
    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSingUpExtendedBinding {
        return FragmentSingUpExtendedBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
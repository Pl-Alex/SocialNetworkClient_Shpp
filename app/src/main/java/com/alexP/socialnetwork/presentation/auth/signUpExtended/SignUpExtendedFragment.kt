package com.alexP.socialnetwork.presentation.auth.signUpExtended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.alexP.socialnetwork.databinding.FragmentSignUpExtendedBinding
import com.alexP.socialnetwork.presentation.base.BaseFragment
import com.alexp.webapi.models.state.ResponseState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpExtendedFragment : BaseFragment<FragmentSignUpExtendedBinding>() {

    private val viewModel: SignUpExtendedViewModel by viewModel()

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSignUpExtendedBinding {
        return FragmentSignUpExtendedBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        with(binding) {
            buttonForward.setOnClickListener {
                viewModel.checkDataAndEditUser()
            }
            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }
            inputEditTextPhone.addTextChangedListener { viewModel.updatePhoneFormField(it.toString()) }
            inputEditTextUserName.addTextChangedListener { viewModel.updateUserNameFormField(it.toString()) }

        }
    }

    private fun setObservers() {
        viewModel.responseState.observe(viewLifecycleOwner)
        { state ->
            when (state) {
                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(SignUpExtendedFragmentDirections.actionSignUpExtendedFragmentToNavGraph())
                    resetViewModelState()
                }

                is ResponseState.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                ResponseState.Initial -> {}
                ResponseState.Loading -> binding.progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.singUpExtendedFormState.observe(viewLifecycleOwner)
        { state ->
            with(binding) {
                inputLayoutPhone.error = state.phoneError?.let { getString(it) }
                inputLayoutUserName.error = state.userNameError?.let { getString(it) }
            }
        }
    }

    private fun resetViewModelState() {
        viewModel.resetRegistrationState()
    }
}
package com.alexP.socialnetwork.ui.auth.signUp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexp.webapi.ApiService
import com.alexp.webapi.repository.MainRepository
import com.alexP.socialnetwork.databinding.FragmentSignUpBinding
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.ui.state.RequestState
import com.alexP.socialnetwork.utils.getValidationResultMessage
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validator.base.ValidationResult
import com.alexp.webapi.BASE_URL
import com.alexp.webapi.getOkHttpClient
import com.alexp.webapi.getRetrofitInstance

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val viewModel: SignUpViewModel by viewModels {
        SignUpViewModel.createFactory(
            DataStoreProvider(requireContext()),
            MainRepository(
                getRetrofitInstance(
                    BASE_URL,
                    getOkHttpClient()
                ).create(ApiService::class.java)
            )
        )
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpExtendedFragment())
        setListeners()
        setObservers()
        resetViewModelState()
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        with(binding) {
            buttonRegister.setOnClickListener {
                onRegisterButtonPressed()
            }
            buttonSignInGoogle.setOnClickListener {
                inputEditTextEmail.setText("123@gmail.com")
                inputEditTextPassword.setText("123@dwddWD")
            }

            inputEditTextEmail.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validateEmail() }
            inputEditTextPassword.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validatePassword() }
        }
    }

    private fun setObservers() {
        viewModel.requestState.observe(viewLifecycleOwner)
        { state ->
            when (state) {
                RequestState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpExtendedFragment())
                }

                is RequestState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                RequestState.Initial -> {}
                RequestState.Loading -> binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun onRegisterButtonPressed() {
        if (!isAnyEnteredDataInvalid()) {
            viewModel.createUser(
                binding.inputEditTextEmail.text.toString().lowercase(),
                binding.inputEditTextPassword.text.toString()
            )
        }
    }

    private fun isAnyEnteredDataInvalid(): Boolean {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        return !(isEmailValid && isPasswordValid)
    }

    private fun validateEmail(): Boolean {
        val validationResult = viewModel.validateEmailVm(binding.inputEditTextEmail.text.toString())
        binding.inputLayoutEmail.error =
            getValidationResultMessage(validationResult)?.let { getString(it) } ?: ""
        return validationResult == ValidationResult.SUCCESS
    }

    private fun validatePassword(): Boolean {
        val validationResult =
            viewModel.validatePasswordVm(binding.inputEditTextPassword.text.toString())
        binding.inputLayoutPassword.error =
            getValidationResultMessage(validationResult)?.let { getString(it) } ?: ""
        return validationResult == ValidationResult.SUCCESS
    }

    private fun resetViewModelState() {
        viewModel.resetRegistrationState()
    }
}
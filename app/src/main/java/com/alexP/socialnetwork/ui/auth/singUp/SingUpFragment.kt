package com.alexP.socialnetwork.ui.auth.singUp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexP.socialnetwork.data.ApiService
import com.alexP.socialnetwork.data.repository.MainRepository
import com.alexP.socialnetwork.databinding.FragmentSingUpBinding
import com.alexP.socialnetwork.ui.auth.singUp.SingUpViewModel.RegistrationState
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.utils.getValidationResultMessage
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validator.base.ValidationResult

class SingUpFragment : BaseFragment<FragmentSingUpBinding>() {

    private val vm: SingUpViewModel by viewModels {
        SingUpViewModel.createFactory(
            DataStoreProvider(requireContext()),
            MainRepository(ApiService.getInstance())
        )
    }

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentSingUpBinding {
        return FragmentSingUpBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setListeners() {
        with(binding) {
            buttonRegister.setOnClickListener {
                onRegisterButtonPressed()
            }
            buttonSingInGoogle.setOnClickListener {
                inputEditTextEmail.setText("123@gmail.com")
                inputEditTextPassword.setText("123@dwddWD")
            }

            inputEditTextEmail.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validateEmail() }
            inputEditTextPassword.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validatePassword() }
        }

        vm.registrationState.observe(viewLifecycleOwner)
        { state ->
            when (state) {
                RegistrationState.SignUpSuccess -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(SingUpFragmentDirections.actionSingUpFragmentToSingUpExtendedFragment())
                    viewModelStore.clear()
                }

                is RegistrationState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                RegistrationState.Initial -> {}
                RegistrationState.Loading -> binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun onRegisterButtonPressed() {
        if (isAnyEnteredDataInvalid()) return
        val emailText = binding.inputEditTextEmail.text.toString().lowercase()
        val passwordText = binding.inputEditTextPassword.text.toString()

        vm.createUser(emailText, passwordText)
    }

    private fun isAnyEnteredDataInvalid(): Boolean {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        return !(isEmailValid && isPasswordValid)
    }

    private fun validateEmail(): Boolean {
        val validationResult = vm.validateEmailVm(binding.inputEditTextEmail.text.toString())
        binding.inputLayoutEmail.error =
            getValidationResultMessage(validationResult)?.let { getString(it) } ?: ""
        return validationResult == ValidationResult.SUCCESS
    }

    private fun validatePassword(): Boolean {
        val validationResult = vm.validatePasswordVm(binding.inputEditTextPassword.text.toString())
        binding.inputLayoutPassword.error =
            getValidationResultMessage(validationResult)?.let { getString(it) } ?: ""
        return validationResult == ValidationResult.SUCCESS
    }
}
package com.alexP.socialnetwork.presentation.auth.signUpExtended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.alexP.socialnetwork.databinding.FragmentSignUpExtendedBinding
import com.alexP.socialnetwork.presentation.base.BaseFragment
import com.alexP.socialnetwork.presentation.state.RequestState
import com.alexP.socialnetwork.utils.getValidationResultMessage
import com.alexp.textvalidation.validator.base.ValidationResult
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
        resetViewModelState()
    }

    private fun setListeners() {
        with(binding) {
            buttonForward.setOnClickListener {
                onForwardButtonPressed()
            }
            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }
            inputEditTextUserName.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validateUserName() }
            inputEditTextPhone.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validatePhone() }
        }
    }

    private fun setObservers() {
        viewModel.requestState.observe(viewLifecycleOwner)
        { state ->
            when (state) {
                RequestState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(SignUpExtendedFragmentDirections.actionSignUpExtendedFragmentToNavGraph())
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

    private fun onForwardButtonPressed() {
        if (!isAnyEnteredDataInvalid()) {
            viewModel.editUser(
                binding.inputEditTextPhone.text.toString(),
                binding.inputEditTextPhone.text.toString()
            )
        }
    }

    private fun isAnyEnteredDataInvalid(): Boolean {
        val isUsernameValid = validateUserName()
        val isPhoneValid = validatePhone()
        return !(isUsernameValid && isPhoneValid)
    }

    private fun validateUserName(): Boolean {
        val validationResult = viewModel.validateUserNameVm(binding.inputEditTextUserName.text.toString())
        binding.inputLayoutUserName.error =
            getValidationResultMessage(validationResult)?.let { getString(it) } ?: ""
        return validationResult == ValidationResult.SUCCESS
    }

    private fun validatePhone(): Boolean {
        val validationResult = viewModel.validatePhoneVm(binding.inputEditTextPhone.text.toString())
        binding.inputLayoutPhone.error =
            getValidationResultMessage(validationResult)?.let { getString(it) } ?: ""
        return validationResult == ValidationResult.SUCCESS
    }

    private fun resetViewModelState() {
        viewModel.resetRegistrationState()
    }
}
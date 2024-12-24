package com.alexP.socialnetwork.ui.auth.singUpExtended

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.alexP.socialnetwork.data.ApiService
import com.alexP.socialnetwork.data.models.RequestState
import com.alexP.socialnetwork.data.repository.MainRepository
import com.alexP.socialnetwork.databinding.FragmentSingUpExtendedBinding
import com.alexP.socialnetwork.ui.base.BaseFragment
import com.alexP.socialnetwork.utils.getValidationResultMessage
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validator.base.ValidationResult

class SingUpExtendedFragment : BaseFragment<FragmentSingUpExtendedBinding>() {

    private val vm: SingUpExtendedViewModel by viewModels {
        SingUpExtendedViewModel.createFactory(
            DataStoreProvider(requireContext()),
            MainRepository(ApiService.getInstance())
        )
    }

    override fun inflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSingUpExtendedBinding {
        return FragmentSingUpExtendedBinding.inflate(inflater, container, false)
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
        vm.requestState.observe(viewLifecycleOwner)
        { state ->
            when (state) {
                RequestState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(SingUpExtendedFragmentDirections.actionSingUpExtendedFragmentToNavGraph())
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
            vm.editUser(
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
        val validationResult = vm.validateUserNameVm(binding.inputEditTextUserName.text.toString())
        binding.inputLayoutUserName.error =
            getValidationResultMessage(validationResult)?.let { getString(it) } ?: ""
        return validationResult == ValidationResult.SUCCESS
    }

    private fun validatePhone(): Boolean {
        val validationResult = vm.validatePhoneVm(binding.inputEditTextPhone.text.toString())
        binding.inputLayoutPhone.error =
            getValidationResultMessage(validationResult)?.let { getString(it) } ?: ""
        return validationResult == ValidationResult.SUCCESS
    }

    private fun resetViewModelState() {
        vm.resetRegistrationState()
    }
}
package com.alexP.socialnetwork.presentation.auth.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.alexP.socialnetwork.databinding.FragmentSignUpBinding
import com.alexP.socialnetwork.presentation.base.BaseFragment
import com.alexp.webapi.models.state.ResponseState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val viewModel: SignUpViewModel by viewModel()

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentSignUpBinding {
        return FragmentSignUpBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        with(binding) {
            buttonRegister.setOnClickListener {
                viewModel.checkDataAndCreateUser()
            }
            inputEditTextEmail.addTextChangedListener { viewModel.updateEmailFormField(it.toString()) }
            inputEditTextPassword.addTextChangedListener { viewModel.updatePasswordFormField(it.toString()) }

            buttonSignInGoogle.setOnClickListener {
                inputEditTextEmail.setText("123test@gmail.com")
                inputEditTextPassword.setText("123test@")
            }
        }
    }

    private fun setObservers() {
        viewModel.responseState.observe(viewLifecycleOwner)
        { state ->
            when (state) {
                is ResponseState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignUpExtendedFragment())
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

        viewModel.singUpFormState.observe(viewLifecycleOwner) { state ->
            binding.inputLayoutEmail.error = state.emailError?.let { getString(it) }
            binding.inputLayoutPassword.error = state.passwordError?.let { getString(it) }
        }
    }

    private fun resetViewModelState() {
        viewModel.resetRegistrationState()
    }
}
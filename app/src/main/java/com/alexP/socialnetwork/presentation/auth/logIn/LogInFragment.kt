package com.alexP.socialnetwork.presentation.auth.logIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.alexP.socialnetwork.databinding.FragmentLogInBinding
import com.alexP.socialnetwork.presentation.base.BaseFragment
import com.alexP.socialnetwork.presentation.state.RequestState
import org.koin.androidx.viewmodel.ext.android.viewModel

class LogInFragment : BaseFragment<FragmentLogInBinding>() {

    private val viewModel: LogInViewModel by viewModel()

    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentLogInBinding {
        return FragmentLogInBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        resetViewModelState()
    }

    private fun setObservers() {
        viewModel.requestState.observe(viewLifecycleOwner)
        { state ->
            when (state) {
                RequestState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToNavGraph())
                }

                is RequestState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }

                RequestState.Initial -> {}
                RequestState.Loading -> binding.progressBar.visibility = View.VISIBLE
            }
        }

        viewModel.logInFormState.observe(viewLifecycleOwner) { state ->
            val emailError = if (state.emailError != null) getString(state.emailError) else null
            binding.inputLayoutEmail.error = emailError

            val passwordError = if (state.passwordError != null) getString(state.passwordError) else null
            binding.inputLayoutPassword.error = passwordError
        }
    }

    private fun setListeners() {
        with(binding) {
            buttonRegister.setOnClickListener {
                viewModel.authorize()
            }
            textViewSignUp.setOnClickListener {
                findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
            }

            inputEditTextEmail.addTextChangedListener{
                viewModel.updateEmailFormField(binding.inputEditTextEmail.text.toString())
            }
            inputEditTextPassword.addTextChangedListener{
                viewModel.updatePasswordFormField(binding.inputEditTextPassword.text.toString())
            }
        }
    }

    private fun resetViewModelState() {
        viewModel.resetRequestState()
    }
}
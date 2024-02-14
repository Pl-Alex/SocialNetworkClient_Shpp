package com.alexP.socialnetwork.ui.screens.signup

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.alexP.socialnetwork.databinding.ActivityAuthBinding
import com.alexP.socialnetwork.ui.base.BaseActivity
import com.alexP.socialnetwork.ui.mainactivity.MainActivity
import com.alexP.socialnetwork.utils.getValidationResultMessage
import com.alexp.datastore.DataStoreProvider
import com.alexp.textvalidation.validator.base.ValidationResult
import kotlinx.coroutines.launch

class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    private val vm: AuthViewModel by viewModels {
        AuthViewModel.createFactory(DataStoreProvider(this))
    }

    override fun inflate(inflater: LayoutInflater): ActivityAuthBinding {
        return ActivityAuthBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                vm.authState.collect { authState ->
                    if (authState.isAutologin) {
                        navToNextScreen()
                    }
                }
            }
        }

        super.onCreate(savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            buttonRegister.setOnClickListener {
                onRegisterButtonPressed()
            }
            buttonSingInGoogle.setOnClickListener {
                Toast.makeText(
                    this@AuthActivity,
                    "Sing in Google button pressed",
                    Toast.LENGTH_SHORT
                ).show()
            }

            inputEditTextEmail.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validateEmail() }
            inputEditTextPassword.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) validatePassword() }
        }
    }

    private fun onRegisterButtonPressed() {
        if (isAnyEnteredDataInvalid()) return
        val emailText = binding.inputEditTextEmail.text.toString().lowercase()
        val passwordText = binding.inputEditTextPassword.text.toString()


        if (binding.checkBoxRemember.isChecked) {
            vm.saveCredentials(emailText, passwordText)
        }else{
            vm.saveCredentials(emailText, "")
        }

        onNavigate()
    }

    private fun isAnyEnteredDataInvalid(): Boolean {
        val isEmailValid = validateEmail()
        val isPasswordValid = validatePassword()
        return !(isEmailValid && isPasswordValid)
    }

    private fun onNavigate() {
        lifecycleScope.launch {
            vm.authState.collect {
                navToNextScreen()
            }
        }
    }

    private fun navToNextScreen() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
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
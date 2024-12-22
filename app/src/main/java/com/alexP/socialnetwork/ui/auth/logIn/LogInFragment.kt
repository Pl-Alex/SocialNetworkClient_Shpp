package com.alexP.socialnetwork.ui.auth.logIn

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alexP.socialnetwork.databinding.FragmentLogInBinding
import com.alexP.socialnetwork.ui.base.BaseFragment

class LogInFragment : BaseFragment<FragmentLogInBinding>() {
    override fun inflate(inflater: LayoutInflater, container: ViewGroup?): FragmentLogInBinding {
        return FragmentLogInBinding.inflate(inflater, container, false)
    }
}
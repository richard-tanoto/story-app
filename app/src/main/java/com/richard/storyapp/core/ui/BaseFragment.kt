package com.richard.storyapp.core.ui

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.richard.storyapp.R

abstract class BaseFragment: Fragment() {

    fun navigateToLogin(fragmentId: Int) {
        findNavController().popBackStack(fragmentId, true)
        findNavController().navigate(R.id.loginFragment)
    }
}
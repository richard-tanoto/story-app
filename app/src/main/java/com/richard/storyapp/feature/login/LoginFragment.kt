package com.richard.storyapp.feature.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.ui.BaseFragment
import com.richard.storyapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickEventListener()
        setupObserver()
    }

    private fun setClickEventListener() {
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.tfEmail.text.toString()
            val password = binding.tfPassword.text.toString()
            if (checkCredentials(email, password)) {
                viewModel.login(email, password)
            } else {
                showToast("Invalid credentials")
            }
        }
    }

    private fun setupObserver() {
        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Success -> {
                    showLoading(false)
                    viewModel.setToken(response.data.loginResult.token)
                }

                is ApiResult.Loading -> {
                    showLoading(true) { viewModel.cancelRequest() }
                }

                is ApiResult.Error -> {
                    showLoading(false)
                    showToast(response.message)
                }
            }
        }
    }

    private fun checkCredentials(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 8
    }

}
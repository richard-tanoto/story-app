package com.richard.storyapp.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richard.storyapp.core.data.AuthRepository
import com.richard.storyapp.core.data.local.preference.PreferencesManager
import com.richard.storyapp.core.data.remote.request.LoginRequest
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.data.remote.response.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _loginResponse = MutableLiveData<ApiResult<LoginResponse>>()
    val loginResponse: LiveData<ApiResult<LoginResponse>> get() = _loginResponse

    fun login(email: String, password: String) = viewModelScope.launch {
        authRepository.login(LoginRequest(email, password)).collect {
            _loginResponse.value = it
        }
    }

    fun cancelRequest() {
        viewModelScope.coroutineContext.cancelChildren()
        _loginResponse.value = ApiResult.Error("Cancelled")
    }

    fun setToken(token: String) = viewModelScope.launch {
        preferencesManager.setToken(token)
    }

}
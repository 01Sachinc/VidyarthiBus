package com.keerthi.vidyarthibus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.keerthi.vidyarthibus.domain.usecase.LoginUseCase
import com.keerthi.vidyarthibus.domain.usecase.RegisterUseCase
import com.keerthi.vidyarthibus.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val authState: StateFlow<Resource<FirebaseUser>?> = _authState

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            _authState.value = loginUseCase(email, pass)
        }
    }

    fun register(name: String, email: String, pass: String, route: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            _authState.value = registerUseCase(name, email, pass, route)
        }
    }
    
    fun resetState() {
        _authState.value = null
    }
}

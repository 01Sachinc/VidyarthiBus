package com.keerthi.vidyarthibus.domain.usecase

import com.keerthi.vidyarthibus.data.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(name: String, email: String, pass: String, route: String) = 
        repository.register(name, email, pass, route)
}

package com.keerthi.vidyarthibus.domain.usecase

import com.keerthi.vidyarthibus.data.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String) = repository.login(email, pass)
}

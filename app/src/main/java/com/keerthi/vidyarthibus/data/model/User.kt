package com.keerthi.vidyarthibus.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val route: String = "",
    val isPremium: Boolean = false,
    val points: Int = 0
)

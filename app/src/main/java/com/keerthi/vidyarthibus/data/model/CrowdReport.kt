package com.keerthi.vidyarthibus.data.model

data class CrowdReport(
    val userId: String = "",
    val status: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

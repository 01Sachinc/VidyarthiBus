package com.keerthi.vidyarthibus.domain.usecase

import com.keerthi.vidyarthibus.data.repository.BusRepository
import javax.inject.Inject

class ReportCrowdUseCase @Inject constructor(
    private val repository: BusRepository
) {
    suspend operator fun invoke(routeId: String, userId: String, status: String) = 
        repository.reportCrowd(routeId, userId, status)
}

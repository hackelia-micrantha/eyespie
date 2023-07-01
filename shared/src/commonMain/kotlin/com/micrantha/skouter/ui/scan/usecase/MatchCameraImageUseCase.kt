package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.skouter.domain.model.ThingMatches
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.domain.repository.ThingRepository
import com.micrantha.skouter.platform.CameraImage

class MatchCameraImageUseCase(
    private val thingRepository: ThingRepository,
    private val clueRepository: ClueRepository
) {
    suspend operator fun invoke(image: CameraImage): Result<ThingMatches> =
        dispatchUseCase(always = {
            image.release()
        }) {
            val proof = clueRepository.match(image).getOrNull()
            if (proof.isNullOrEmpty()) {
                return@dispatchUseCase emptyList()
            }
            val clue = proof.first()
            val results = thingRepository.match(clue.data).getOrThrow()
            results.sortedBy { it.similarity }
        }
}

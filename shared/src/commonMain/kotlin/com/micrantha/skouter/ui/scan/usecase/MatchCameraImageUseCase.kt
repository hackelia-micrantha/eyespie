package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.skouter.domain.repository.ClueRepository
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.platform.ImageEmbedding
import kotlin.math.sqrt

class MatchCameraImageUseCase(
    private val clueRepository: ClueRepository
) {
    suspend operator fun invoke(
        image: CameraImage,
        embedding: ImageEmbedding
    ): Result<Boolean> =
        dispatchUseCase {
            val proof = clueRepository.match(image).getOrNull()
            if (proof.isNullOrEmpty()) {
                return@dispatchUseCase false
            }
            val clue = proof.first()

            val result = similarity(clue.data.toByteArray(), embedding.toByteArray())

            Log.i("image match similarity: $result")

            result.compareTo(0.70000000) >= 0
        }

    private fun similarity(vectorA: ByteArray, vectorB: ByteArray): Double {
        require(vectorA.size == vectorB.size) { "Vector dimensions must be the same" }

        var dotProduct = 0L
        var normA = 0L
        var normB = 0L

        for (i in vectorA.indices) {
            dotProduct += vectorA[i] * vectorB[i]
            normA += vectorA[i].toLong() * vectorA[i].toLong()
            normB += vectorB[i].toLong() * vectorB[i].toLong()
        }

        val normProduct = sqrt(normA.toDouble()) * sqrt(normB.toDouble())

        return if (normProduct != 0.0) dotProduct.toDouble() / normProduct else 0.0

    }
}

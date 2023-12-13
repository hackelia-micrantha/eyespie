package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.skouter.domain.repository.MatchRepository
import com.micrantha.skouter.platform.scan.CameraImage
import com.micrantha.skouter.platform.scan.model.ImageEmbedding
import kotlin.coroutines.coroutineContext

import kotlin.math.sqrt

class MatchCaptureUseCase(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(
        image: CameraImage,
        embedding: ImageEmbedding
    ): Result<Boolean> =
        dispatchUseCase(coroutineContext) {
            val clue = matchRepository.analyze(image).getOrNull() ?: return@dispatchUseCase false

            val result = similarity(clue.first().data.toByteArray(), embedding.toByteArray())

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

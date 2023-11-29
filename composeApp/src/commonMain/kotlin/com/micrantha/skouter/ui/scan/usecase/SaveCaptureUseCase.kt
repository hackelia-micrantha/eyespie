package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.repository.StorageRepository
import com.micrantha.skouter.domain.repository.ThingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.Path.Companion.toPath

class SaveCaptureUseCase(
    private val storageRepository: StorageRepository,
    private val thingRepository: ThingRepository,
    private val fileSystem: FileSystem
) {

    suspend operator fun invoke(
        proof: Proof
    ) = dispatchUseCase {
        val image = withContext(Dispatchers.IO) {
            fileSystem.read(proof.image)
        }

        storageRepository.upload(
            "${proof.playerID}/${proof.image.name}.jpg",
            image
        ).map { url ->
            thingRepository.create(proof.copy(image = url.toPath())).getOrThrow()
        }
    }
}

package com.micrantha.skouter.ui.scan.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.platform.CameraImage
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.repository.StorageRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import io.github.aakira.napier.Napier

class SaveThingImageUseCase(
    private val storageRepository: StorageRepository,
    private val currentAccount: CurrentSession,
    private val thingsRepository: ThingsRepository
) {

    suspend operator fun invoke(
        image: CameraImage,
        name: String = uuid4().toString()
    ): Result<Thing> = try {
        val user = currentAccount.requirePlayer()

        val data = image.toByteArray()

        storageRepository.upload("${user.id}/${name}.jpg", data).map {
            thingsRepository.create(name, it, user.id).getOrThrow()
        }

    } catch (err: Throwable) {
        Napier.e("upload image", err)
        Result.failure(err)
    }
}

package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.Platform
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.repository.StorageRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import io.github.aakira.napier.Napier
import okio.Path

class SaveThingImageUseCase(
    private val storageRepository: StorageRepository,
    private val currentAccount: CurrentSession,
    private val thingsRepository: ThingsRepository,
    private val platform: Platform,
) {

    suspend operator fun invoke(
        name: String,
        path: Path
    ): Result<Thing> = try {
        val user = currentAccount.requirePlayer()

        val data = platform.read(path)

        storageRepository.upload("${user.id}/${path.name}.jpg", data).map {
            thingsRepository.create(name, it, user.id).getOrThrow()
        }

    } catch (err: Throwable) {
        Napier.e("upload image", err)
        Result.failure(err)
    }
}

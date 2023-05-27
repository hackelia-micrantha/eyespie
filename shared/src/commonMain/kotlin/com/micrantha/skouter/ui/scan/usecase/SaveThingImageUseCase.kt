package com.micrantha.skouter.ui.scan.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.StorageRepository
import com.micrantha.skouter.domain.repository.ThingsRepository
import io.github.aakira.napier.Napier

class SaveThingImageUseCase(
    private val storageRepository: StorageRepository,
    private val accountRepository: AccountRepository,
    private val thingsRepository: ThingsRepository
) {

    suspend operator fun invoke(
        name: String,
        data: ByteArray,
        fileName: String = uuid4().toString()
    ): Result<Thing> = try {
        val user = accountRepository.currentPlayer!!

        storageRepository.upload("${user.id}/${fileName}.jpg", data).map {
            thingsRepository.create(name, it, user.id).getOrThrow()
        }

    } catch (err: Throwable) {
        Napier.e("upload image", err)
        Result.failure(err)
    }
}

package com.micrantha.skouter.ui.scan.usecase

import com.benasher44.uuid.uuid4
import com.micrantha.skouter.domain.repository.AccountRepository
import com.micrantha.skouter.domain.repository.StorageRepository
import io.github.aakira.napier.Napier

class UploadImageUseCase(
    private val storageRepository: StorageRepository,
    private val accountRepository: AccountRepository
) {

    suspend operator fun invoke(
        data: ByteArray,
        name: String = uuid4().toString()
    ): Result<String> = try {
        val playerID = accountRepository.currentPlayer!!.id
        storageRepository.upload("${playerID}/${name}.jpg", data)
    } catch (err: Throwable) {
        Napier.e("upload image", err)
        Result.failure(err)
    }
}

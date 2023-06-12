package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.domain.usecase.dispatchUseCase
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.repository.StorageRepository
import com.micrantha.skouter.domain.repository.ThingRepository

class SaveThingImageUseCase(
    private val storageRepository: StorageRepository,
    private val currentSession: CurrentSession,
    private val thingRepository: ThingRepository,
    private val fileSystem: FileSystem
) {

    suspend operator fun invoke(
        thing: Thing.Create
    ) = dispatchUseCase {
        val user = currentSession.requirePlayer()

        val location = user.location!!

        val image = fileSystem.read(thing.path)

        storageRepository.upload(
            "${user.id}/${thing.name}.jpg",
            image
        ).map { url ->
            thingRepository.create(thing, url, user.id, location).getOrThrow()
        }
    }
}

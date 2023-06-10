package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.bluebell.data.Log
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.repository.StorageRepository
import com.micrantha.skouter.domain.repository.ThingRepository
import com.micrantha.skouter.platform.CameraImage

class SaveThingImageUseCase(
    private val storageRepository: StorageRepository,
    private val currentSession: CurrentSession,
    private val thingRepository: ThingRepository,
) {

    suspend operator fun invoke(
        thing: Thing.Create,
        image: CameraImage
    ): Result<Thing> = try {
        val user = currentSession.requirePlayer()

        val location = user.location!!

        storageRepository.upload(
            "${user.id}/${thing.name}.jpg",
            image.toByteArray()
        ).map { url ->
            thingRepository.create(thing, url, user.id, location).getOrThrow()
        }
    } catch (err: Throwable) {
        Log.e("upload image", err)
        Result.failure(err)
    }
}

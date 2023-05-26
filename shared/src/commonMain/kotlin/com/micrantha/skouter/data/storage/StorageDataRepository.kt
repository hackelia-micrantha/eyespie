package com.micrantha.skouter.data.storage

import com.micrantha.skouter.data.storage.source.StorageLocalSource
import com.micrantha.skouter.data.storage.source.StorageRemoteSource
import com.micrantha.skouter.domain.model.Image
import com.micrantha.skouter.domain.repository.StorageRepository

class StorageDataRepository(
    private val remoteSource: StorageRemoteSource,
    private val localSource: StorageLocalSource
) : StorageRepository {

    override suspend fun download(image: Image) =
        remoteSource.download(image.bucketId, image.path).onSuccess {
            localSource.put(image.id, it)
        }

    override fun get(image: Image) = localSource.get(image.id)
}

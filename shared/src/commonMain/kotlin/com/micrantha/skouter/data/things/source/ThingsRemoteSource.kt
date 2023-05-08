package com.micrantha.skouter.data.things.source

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.micrantha.skouter.data.remote.MicranthaClient
import com.micrantha.skouter.data.remote.SupaClient
import com.micrantha.skouter.data.things.mapping.ThingsDomainMapper
import com.micrantha.skouter.data.things.model.RecognitionResponse
import com.micrantha.skouter.data.things.model.path
import com.micrantha.skouter.domain.models.Clues
import com.micrantha.skouter.domain.models.Thing.Image
import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import org.jetbrains.skia.Image as ImageBuilder

class ThingsRemoteSource(
    private val client: MicranthaClient,
    private val supabase: SupaClient,
    private val mapper: ThingsDomainMapper
) {
    suspend fun recognize(image: ByteArray, contentType: String): Result<Clues> =
        try {
            val response: RecognitionResponse = client.recognize(image, contentType).body()
            Result.success(mapper(response))
        } catch (err: Throwable) {
            Napier.e("recognize", err)
            Result.failure(err)
        }

    suspend fun image(image: Image): Result<BitmapPainter> = try {
        val result = supabase.storage().get(image.bucketId)
            .downloadAuthenticated(image.path).let {
                ImageBuilder.Companion.makeFromEncoded(it)
                    .toComposeImageBitmap().let(::BitmapPainter)
            }
        Result.success(result)
    } catch (err: Throwable) {
        Napier.e("image", err)
        Result.failure(err)
    }
}

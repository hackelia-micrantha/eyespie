package com.micrantha.eyespie.features.scan.ui.capture

import androidx.compose.ui.graphics.painter.BitmapPainter
import com.micrantha.bluebell.app.Log
import com.micrantha.bluebell.arch.StateMapper
import com.micrantha.bluebell.domain.stats.weightedRandomSample
import com.micrantha.eyespie.domain.entities.Clue
import com.micrantha.eyespie.domain.entities.Clues
import com.micrantha.eyespie.domain.entities.Embedding
import com.micrantha.eyespie.domain.entities.LabelProof
import com.micrantha.eyespie.domain.entities.LocationClue
import com.micrantha.eyespie.domain.entities.Proof

class ScanCaptureStateMapper : StateMapper<ScanState, ScanUiState> {

    override fun map(state: ScanState): ScanUiState = try {
        ScanUiState(
            clues = clues(state),
            overlays = overlays(state),
            enabled = state.enabled,
            busy = state.busy,
            capture = if (state.enabled.not()) (state.obfuscated ?: state.image)?.toImageBitmap()
                ?.let {
                BitmapPainter(it)
            } else null
        )
    } catch (err: Throwable) {
        Log.e("mapping", err) { "unable to map scan state" }
        throw err
    }

    fun prove(state: ScanState) = Proof(
        clues = Clues(
            labels = state.labels,
            location = state.location?.data?.let { LocationClue(it) },
            colors = state.colors
        ),
        name = state.labels?.minByOrNull { it.confidence }?.data,
        image = state.path!!,
        match = state.match?.data ?: Embedding.EMPTY,
        location = state.location?.point,
        playerID = state.playerID!!
    )

    private fun LabelProof.sample() = this.weightedRandomSample {
        it.confidence.toDouble()
    }

    private fun <T : Clue<*>> format(item: T?, size: Int): String? = when {
        size == 0 || item == null -> null
        size == 1 -> "What: ${item.display()}"
        else -> "What: ${item.display()} +${size - 1}"
    }

    private fun clues(state: ScanState) = mutableListOf<String>().apply {
        state.labels?.let { labels ->
            format(labels.sample(), labels.size)?.let {
                add(it)
            }
        }
        state.colors?.let { colors ->
            format(colors.randomOrNull(), colors.size)?.let {
                add(it)
            }
        }
        state.location?.let { add("Location: ${it.point}") }
    }

    private fun overlays(state: ScanState): List<ScanOverlay> {
        val result = mutableListOf<ScanOverlay>()
        if (state.enabled.not()) return result

        if (state.image != null) {
            state.detection?.let {
                result.add(
                    ScanBox(
                        it.data,
                        it.labels.firstOrNull()?.display() ?: "",
                        imageWidth = state.image.width,
                        imageHeight = state.image.height
                    )
                )
            }
        }
        state.segment?.let {
            result.add(ScanMask(it.data))
        }
        return result
    }
}

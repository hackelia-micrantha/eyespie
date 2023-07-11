package com.micrantha.skouter.ui.scan.preview

import com.micrantha.bluebell.data.weightedRandomSample
import com.micrantha.bluebell.ui.screen.StateMapper
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.LocationClue
import com.micrantha.skouter.domain.model.Proof

class ScanStateMapper : StateMapper<ScanState, ScanUiState> {

    override fun map(state: ScanState) = ScanUiState(
        clues = clues(state),
        overlays = overlays(state),
        enabled = state.enabled
    )

    fun prove(state: ScanState) = Proof(
        clues = Clues(
            labels = state.labels,
            location = state.location?.data?.let { LocationClue(it) },
            colors = state.colors
        ),
        name = "",
        image = state.path!!,
        match = state.match!!.data,
        location = state.location?.point,
        playerID = state.playerID!!
    )

    private fun LabelProof.sample() = this.weightedRandomSample {
        it.confidence.toDouble()
    }

    private fun <T> format(item: T?, size: Int): String? = when {
        size == 0 || item == null -> null
        size == 1 -> "What: $item"
        else -> "What: $item +${size - 1}"
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
        if (state.image == null) return result

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
        state.segment?.let {
            result.add(ScanMask(it.data))
        }
        return result
    }
}

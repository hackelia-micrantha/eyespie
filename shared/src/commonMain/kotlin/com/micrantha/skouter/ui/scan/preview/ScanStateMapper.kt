package com.micrantha.skouter.ui.scan.preview

import com.micrantha.bluebell.data.weightedRandomSample
import com.micrantha.bluebell.data.weightedTo
import com.micrantha.bluebell.ui.screen.StateMapper
import com.micrantha.skouter.domain.model.Clues
import com.micrantha.skouter.domain.model.DetectClue
import com.micrantha.skouter.domain.model.LabelProof
import com.micrantha.skouter.domain.model.LocationClue
import com.micrantha.skouter.domain.model.Proof
import kotlin.math.max

object ScanStateMapper : StateMapper<ScanState, ScanUiState> {

    override fun map(state: ScanState) = ScanUiState(
        clues = clues(state),
        overlays = emptyList(),
        enabled = state.enabled
    )

    fun prove(state: ScanState) = Proof(
        clues = Clues(
            labels = state.labels,
            location = LocationClue(state.location!!.data!!),
            colors = state.colors
        ),
        name = "",
        image = state.path!!,
        match = state.match!!.data,
        location = state.location,
        playerID = state.playerID!!
    )

    private fun LabelProof.sample() = this.weightedRandomSample {
        it.data weightedTo (it.confidence * 10).toInt()
    }

    private fun <T> format(item: T?, size: Int): String? = when {
        size == 0 || item == null -> null
        size == 1 -> "What: $item"
        else -> "What: $item +${size - 1}"
    }

    fun clues(state: ScanState) = mutableListOf<String>().apply {
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

    fun overlays(state: ScanState): List<ScanOverlay> {
        val result = mutableListOf<ScanOverlay>()
        if (state.image == null) return result

        state.detection?.let {
            result.add(it.asScanBoxIn(state.image.width, state.image.height))
        }
        state.segment?.let {
            result.add(ScanMask(it.data))
        }
        return result
    }

    private fun DetectClue.asScanBoxIn(width: Int, height: Int) = ScanBox(
        rect,
        labels.firstOrNull()?.display() ?: "",
        max(rect.width / width, rect.height / height)
    )
}

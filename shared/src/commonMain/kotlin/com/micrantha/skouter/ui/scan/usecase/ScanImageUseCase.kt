package com.micrantha.skouter.ui.scan.usecase

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.skouter.domain.model.Clues

class ScanImageUseCase {
    operator fun invoke(data: Painter): Result<Clues> = Result.failure(NotImplementedError())
}

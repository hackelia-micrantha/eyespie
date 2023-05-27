package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.skouter.domain.model.Clues
import okio.Path

class ScanImageUseCase {
    operator fun invoke(data: Path): Result<Clues> = Result.failure(NotImplementedError())
}

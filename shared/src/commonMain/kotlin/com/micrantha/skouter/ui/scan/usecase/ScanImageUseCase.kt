package com.micrantha.skouter.ui.scan.usecase

import com.micrantha.skouter.domain.model.Clues

class ScanImageUseCase {
    operator fun invoke(data: ByteArray): Result<Clues> = Result.failure(NotImplementedError())
}

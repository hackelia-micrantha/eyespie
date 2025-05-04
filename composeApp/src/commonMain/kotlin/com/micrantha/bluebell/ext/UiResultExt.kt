package com.micrantha.bluebell.ext

import com.micrantha.bluebell.domain.entities.LocalizedString
import com.micrantha.bluebell.domain.repository.LocalizedRepository
import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.bluebell.ui.model.UiResult.Busy
import com.micrantha.bluebell.ui.model.UiResult.Empty
import com.micrantha.bluebell.ui.model.UiResult.Failure
import com.micrantha.bluebell.ui.model.UiResult.Ready

fun LocalizedRepository.busy(str: LocalizedString) = Busy(resource(str))

fun LocalizedRepository.failure(str: LocalizedString) = Failure(resource(str))

fun <T> List<T>.status() = if (isEmpty()) Empty() else Ready(this)

fun <T> UiResult<T>.enabled(): Boolean = this !is Busy



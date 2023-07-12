package com.micrantha.bluebell.domain.ext

import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.bluebell.domain.model.UiResult.Busy
import com.micrantha.bluebell.domain.model.UiResult.Empty
import com.micrantha.bluebell.domain.model.UiResult.Failure
import com.micrantha.bluebell.domain.model.UiResult.Ready

fun LocalizedRepository.busy(str: LocalizedString) = Busy(resource(str))

fun LocalizedRepository.failure(str: LocalizedString) = Failure(resource(str))

fun <T> List<T>.status() = if (isEmpty()) Empty() else Ready(this)

fun <T> UiResult<T>.enabled(): Boolean = this !is Busy



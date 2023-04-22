package com.micrantha.bluebell.domain.status

import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import com.micrantha.bluebell.domain.status.ModelStatus.Busy
import com.micrantha.bluebell.domain.status.ModelStatus.Empty
import com.micrantha.bluebell.domain.status.ModelStatus.Failure
import com.micrantha.bluebell.domain.status.ModelStatus.Ready

fun LocalizedRepository.busy(str: LocalizedString) = Busy(resource(str))

fun LocalizedRepository.error(str: LocalizedString) = Failure(resource(str))

fun <T> Collection<T>.status() = if (isEmpty()) Empty() else Ready(this)

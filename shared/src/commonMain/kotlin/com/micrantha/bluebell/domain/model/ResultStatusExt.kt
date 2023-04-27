package com.micrantha.bluebell.domain.model

import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import com.micrantha.bluebell.domain.model.ResultStatus.Busy
import com.micrantha.bluebell.domain.model.ResultStatus.Empty
import com.micrantha.bluebell.domain.model.ResultStatus.Failure
import com.micrantha.bluebell.domain.model.ResultStatus.Ready

fun LocalizedRepository.busy(str: LocalizedString) = Busy(resource(str))

fun LocalizedRepository.error(str: LocalizedString) = Failure(resource(str))

fun <T> Collection<T>.status() = if (isEmpty()) Empty() else Ready(this)

fun ResultStatus.enabled(): Boolean = this !is ResultStatus.Busy

package com.micrantha.bluebell.ext

import com.micrantha.bluebell.ui.model.UiResult
import com.micrantha.bluebell.ui.model.UiResult.Busy
import com.micrantha.bluebell.ui.model.UiResult.Empty
import com.micrantha.bluebell.ui.model.UiResult.Ready

fun <T> List<T>.status() = if (isEmpty()) Empty() else Ready(this)

fun <T> UiResult<T>.enabled(): Boolean = this !is Busy



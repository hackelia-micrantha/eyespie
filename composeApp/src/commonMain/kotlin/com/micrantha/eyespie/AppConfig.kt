package com.micrantha.eyespie

import com.micrantha.bluebell.platform.AppConfigDelegate
import com.micrantha.bluebell.platform.OptionalAppConfigDelegate
import com.micrantha.eyespie.config.LOGIN_EMAIL
import com.micrantha.eyespie.config.LOGIN_PASSWORD

object AppConfig {
    val LOGIN_EMAIL by OptionalAppConfigDelegate(LOGIN_EMAIL)
    val LOGIN_PASSWORD by OptionalAppConfigDelegate(LOGIN_PASSWORD)
    val SUPABASE_URL by AppConfigDelegate
    val SUPABASE_KEY by AppConfigDelegate
    val HUGGING_FACE_TOKEN by AppConfigDelegate
}
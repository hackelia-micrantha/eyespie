package com.micrantha.eyespie.app

import com.micrantha.bluebell.platform.AppConfigDelegate
import com.micrantha.bluebell.platform.DefaultAppConfigDelegate
import com.micrantha.eyespie.config.getValue

object AppConfig {
    val LOGIN_EMAIL by DefaultAppConfigDelegate("user@example.com")
    val LOGIN_PASSWORD by DefaultAppConfigDelegate("******")
    val SUPABASE_URL by AppConfigDelegate
    val SUPABASE_KEY by AppConfigDelegate
    val HUGGING_FACE_TOKEN by AppConfigDelegate
}
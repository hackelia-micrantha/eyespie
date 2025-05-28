package com.micrantha.eyespie.app

import com.micrantha.bluebell.platform.DefaultAppConfigDelegate
import com.micrantha.bluebell.platform.OptionalAppConfigDelegate

object AppConfig {
    val LOGIN_EMAIL by OptionalAppConfigDelegate
    val LOGIN_PASSWORD by OptionalAppConfigDelegate
    val SUPABASE_URL by DefaultAppConfigDelegate
    val SUPABASE_KEY by DefaultAppConfigDelegate
    val HUGGING_FACE_TOKEN by OptionalAppConfigDelegate
}
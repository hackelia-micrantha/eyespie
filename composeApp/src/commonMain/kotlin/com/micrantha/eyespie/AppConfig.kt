package com.micrantha.eyespie

import com.micrantha.bluebell.platform.AppConfigDelegate
import com.micrantha.bluebell.platform.AppConfigDelegateWithDefault

object AppConfig {
    val LOGIN_EMAIL by AppConfigDelegateWithDefault
    val LOGIN_PASSWORD by AppConfigDelegateWithDefault
    val SUPABASE_URL by AppConfigDelegate
    val SUPABASE_KEY by AppConfigDelegate
    val HUGGING_FACE_TOKEN by AppConfigDelegate
}
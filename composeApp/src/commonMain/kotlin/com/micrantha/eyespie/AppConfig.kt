package com.micrantha.eyespie

import com.micrantha.bluebell.platform.AppConfigDelegate
import com.micrantha.eyespie.config.LOGIN_EMAIL as DEBUG_LOGIN_EMAIL
import com.micrantha.eyespie.config.LOGIN_PASSWORD as DEBUG_LOGIN_PASSWORD

object AppConfig {
    val LOGIN_EMAIL = DEBUG_LOGIN_EMAIL
    val LOGIN_PASSWORD = DEBUG_LOGIN_PASSWORD
    val SUPABASE_URL by AppConfigDelegate
    val SUPABASE_KEY by AppConfigDelegate
    val HUGGING_FACE_TOKEN by AppConfigDelegate
}
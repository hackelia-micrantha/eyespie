package com.micrantha.skouter.data.client

import com.micrantha.skouter.SkouterConfig
import io.ktor.client.request.bearerAuth
import io.ktor.http.URLProtocol

fun huggingFaceClient(model: String) =
    createHttpClient {
        url {
            protocol = URLProtocol.HTTPS
            host = "api-inference.huggingface.co/models/${model}"
        }
        bearerAuth(SkouterConfig.huggingFaceApiToken)
    }

package me.ks.chan.material.symbols.ksp.repository

import com.google.devtools.ksp.processing.KSPLogger
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MaterialSymbolsRepository(
    private val okHttpClient: OkHttpClient, private val kspLogger: KSPLogger
) {

    private inline val String.asRequest: Request
        get() = Request.Builder()
            .get()
            .url(toHttpUrl())
            .build()

    private inline val Request.toResponse: Response
        get() = okHttpClient.newCall(this).execute()

    operator fun invoke(url: String): String {
        kspLogger.info(url)
        return url.asRequest.toResponse.body.string()
    }

}
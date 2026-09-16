package me.ks.chan.material.symbols.ksp.repository

import com.google.devtools.ksp.processing.KSPLogger
import okhttp3.Call
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket

class MaterialSymbolsRepository(okHttpClient: OkHttpClient, kspLogger: KSPLogger):
    Call.Factory by okHttpClient,
    WebSocket.Factory by okHttpClient,
    KSPLogger by kspLogger {

    private inline val String.asRequest: Request
        get() = Request.Builder()
            .get()
            .url(toHttpUrl())
            .build()

    private inline val Request.toResponse: Response
        get() = newCall(this).execute()

    operator fun invoke(url: String): String {
        info(url)
        return url.asRequest.toResponse.body.string()
    }

}
package me.ks.chan.material.symbols.ksp.environment

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import java.io.File
import java.net.InetSocketAddress
import java.net.Proxy
import me.ks.chan.material.symbols.ksp.option.network.CacheOption.cacheDirectory
import me.ks.chan.material.symbols.ksp.option.network.CacheOption.cacheSize
import me.ks.chan.material.symbols.ksp.option.network.ProxyOption.proxyHost
import me.ks.chan.material.symbols.ksp.option.network.ProxyOption.proxyPort
import okhttp3.Cache
import okhttp3.OkHttpClient

fun SymbolProcessorEnvironment.okHttpClient(kspLogger: KSPLogger): OkHttpClient {
    return OkHttpClient.Builder()
        .also { builder: OkHttpClient.Builder ->
            configureProxyOptions(kspLogger)?.let(builder::proxy)
            configureCacheOptions(kspLogger)?.let(builder::cache)
        }
        .build()
}

private fun SymbolProcessorEnvironment.configureProxyOptions(
    kspLogger: KSPLogger
) = proxyHost?.let { proxyHost ->
    val proxyPort = proxyPort
    kspLogger.info("Http proxy is enabled as ${proxyHost}:${proxyPort}.")
    Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHost, proxyPort))
}

private fun SymbolProcessorEnvironment.configureCacheOptions(
    kspLogger: KSPLogger
) = cacheDirectory?.let { cacheDirectory ->
    val cacheSize = cacheSize
    kspLogger.info(
        "MaterialSymbols repository caching is enabled as " +
            cacheSize +
            " bytes at \"" +
            cacheDirectory +
            "\"."
    )
    Cache(File(cacheDirectory), cacheSize)
}

package me.ks.chan.material.symbols.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import me.ks.chan.material.symbols.ksp.environment.okHttpClient

class MaterialSymbolsSymbolProcessorProvider: SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val kspLogger = environment.kspLogger

        return MaterialSymbolsSymbolProcessor(
            codeGenerator = environment.codeGenerator,
            kspLogger = kspLogger,
            okHttpClient = environment.okHttpClient(kspLogger),
        )
    }
}

private inline val SymbolProcessorEnvironment.kspLogger: KSPLogger
    get() = logger.apply {
        info("Kotlin: $apiVersion")
        info("KotlinCompiler: $apiVersion")
        info("KSP: $kspVersion")
        info("Platforms: $platforms")
    }

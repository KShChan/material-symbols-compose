package me.ks.chan.material.symbols.ksp.repository

import com.google.devtools.ksp.processing.KSPLogger
import me.ks.chan.material.symbols.ksp.annotation.MaterialSymbolIcon
import me.ks.chan.material.symbols.ksp.ext.asPascalCase
import okhttp3.OkHttpClient

class MaterialSymbolsUseCase(
    icon: String,
    materialSymbolIcon: MaterialSymbolIcon,
    kspLogger: KSPLogger,
) {

    private val materialSymbolsRepository by lazy {
        GoogleMaterialSymbolsRepository(icon, materialSymbolIcon)
    }

    init {
        icon.asPascalCase.let { iconName ->
            kspLogger.info(
                "Icon=${iconName}: " +
                    "Style=${materialSymbolIcon.style.name}, " +
                    "Weight=${materialSymbolIcon.weight.name}, " +
                    "Grade=${materialSymbolIcon.grade.name}, " +
                    "Filled=${materialSymbolIcon.filled}"
            )
            kspLogger.info("$iconName URL: ${materialSymbolsRepository.repositoryUrl}")
        }
    }

    fun fetch(okHttpClient: OkHttpClient): List<PathBuilderCommand> {
        return materialSymbolsRepository.fetch(okHttpClient) processWith
            VectorDrawableRepository processWith PathBuilderRepository
    }

}
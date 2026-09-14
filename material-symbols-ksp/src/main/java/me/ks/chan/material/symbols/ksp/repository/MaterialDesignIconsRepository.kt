package me.ks.chan.material.symbols.ksp.repository

import com.google.devtools.ksp.processing.KSPLogger
import me.ks.chan.material.symbols.annotation.MaterialSymbolGrade
import me.ks.chan.material.symbols.annotation.MaterialSymbolOpticalSize
import me.ks.chan.material.symbols.annotation.MaterialSymbolStyle
import me.ks.chan.material.symbols.annotation.MaterialSymbolWeight
import me.ks.chan.material.symbols.ksp.annotation.MaterialSymbolIcon
import me.ks.chan.material.symbols.ksp.ext.asPascalCase

class MaterialDesignIconsRepository(private val kspLogger: KSPLogger) {

    operator fun invoke(icon: String, materialSymbolIcon: MaterialSymbolIcon): String {
        kspLogger.info(
            "Icon=${icon.asPascalCase}: " +
                "Style=${materialSymbolIcon.style.name}, " +
                "Weight=${materialSymbolIcon.weight.name}, " +
                "Grade=${materialSymbolIcon.grade.name}, " +
                "Filled=${materialSymbolIcon.filled}"
        )

        return with(materialSymbolIcon) {
            "https://raw.githubusercontent.com/google/material-design-icons/master/symbols/android/" +
                "$icon/materialsymbols$styleUrlOption/$icon${customizationOption}_${opticalSizeInt}px.xml"
        }
    }

}

private inline val MaterialSymbolIcon.styleUrlOption: String
    get() = when (style) {
        MaterialSymbolStyle.Outlined -> "outlined"
        MaterialSymbolStyle.Rounded -> "rounded"
        MaterialSymbolStyle.Sharp -> "sharp"
    }

private inline val MaterialSymbolIcon.customizationOption: String
    get() = when {
        weight == MaterialSymbolWeight.Regular &&
            grade == MaterialSymbolGrade.Regular &&
            !filled -> { "" }
        else -> { "_${weightUrlOption}${gradeUrlOption}${filledUrlOption}" }
    }

private inline val MaterialSymbolIcon.filledUrlOption: String
    get() = if (filled) "fill1" else ""

private inline val MaterialSymbolIcon.weightUrlOption: String
    get() = when (weight) {
        MaterialSymbolWeight.Thinnest -> "wght100"
        MaterialSymbolWeight.Thin -> "wght200"
        MaterialSymbolWeight.Thinner -> "wght300"
        MaterialSymbolWeight.Regular -> ""
        MaterialSymbolWeight.Bold -> "wght500"
        MaterialSymbolWeight.Bolder -> "wght600"
        MaterialSymbolWeight.Boldest -> "wght700"
    }

private inline val MaterialSymbolIcon.gradeUrlOption: String
    get() = when (grade) {
        MaterialSymbolGrade.Low -> "gradN25"
        MaterialSymbolGrade.Regular -> ""
        MaterialSymbolGrade.High -> "grad200"
    }

private inline val MaterialSymbolIcon.opticalSizeInt: Int
    get() = when (opticalSize) {
        MaterialSymbolOpticalSize.Small -> 20
        MaterialSymbolOpticalSize.Regular -> 24
        MaterialSymbolOpticalSize.Large -> 40
        MaterialSymbolOpticalSize.Larger -> 48
    }
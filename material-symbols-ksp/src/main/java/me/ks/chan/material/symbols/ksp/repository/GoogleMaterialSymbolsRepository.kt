package me.ks.chan.material.symbols.ksp.repository

import me.ks.chan.material.symbols.annotation.MaterialSymbolGrade
import me.ks.chan.material.symbols.annotation.MaterialSymbolOpticalSize
import me.ks.chan.material.symbols.annotation.MaterialSymbolStyle
import me.ks.chan.material.symbols.annotation.MaterialSymbolWeight
import me.ks.chan.material.symbols.ksp.annotation.MaterialSymbolIcon
import okhttp3.OkHttpClient
import okhttp3.Request

class GoogleMaterialSymbolsRepository(icon: String, materialSymbolIcon: MaterialSymbolIcon) {

    val repositoryUrl by lazy {
        with(materialSymbolIcon) {
            @Suppress("SpellCheckingInspection")
            "https://raw.githubusercontent.com/google/material-design-icons/master/symbols/android/$icon/materialsymbols" +
                "$styleUrlOption/$icon${customizationOption}_${opticalSizeInt}px.xml"
        }
    }

    private inline val repositoryRequest: Request
        get() = Request.Builder()
            .url(repositoryUrl)
            .get()
            .build()

    fun fetch(okHttpClient: OkHttpClient): String =
        okHttpClient.newCall(repositoryRequest)
            .execute()
            .body
            .string()

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
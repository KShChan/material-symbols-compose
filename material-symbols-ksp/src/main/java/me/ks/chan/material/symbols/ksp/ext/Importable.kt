package me.ks.chan.material.symbols.ksp.ext

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec

internal sealed class Importable {

    enum class NameType { Class, Method }

    abstract val packageName: String

    protected open val classname: String
        get() = this::class.java.simpleName

    fun short(name: NameType = NameType.Class): String = classname.let {
        it.takeIf { name == NameType.Class } ?: it.replaceFirstChar(Char::lowercaseChar)
    }

    fun full(name: NameType = NameType.Class): String {
        return "${packageName}.${short(name)}"
    }

    fun className(name: NameType = NameType.Class): ClassName {
        return ClassName(packageName, short(name))
    }

}

internal data object MaterialSymbols: Importable() {

    override val packageName: String
        get() = "me.ks.chan.material.symbols"

    data object MaterialSymbol: Importable() {
        override val packageName: String
            get() = MaterialSymbols.packageName
    }

}

internal sealed class ComposeUi: Importable() {
    override val packageName: String
        get() = "androidx.compose.ui"
}

internal sealed class ComposeUiGraphics: ComposeUi() {

    override val packageName: String
        get() = "${super.packageName}.graphics"

    data object Color: ComposeUiGraphics()
    data object SolidColor: ComposeUiGraphics()
    data object StrokeJoin: ComposeUiGraphics()

}

internal sealed class ComposeUiVectorGraphics: ComposeUiGraphics() {

    override val packageName: String
        get() = "${super.packageName}.vector"

    data object ImageVector: ComposeUiVectorGraphics()
    data object PathBuilder: ComposeUiVectorGraphics()
    data object Path: ComposeUiVectorGraphics()

}

internal sealed class ComposeUiUnit: ComposeUi() {

    override val packageName: String
        get() = "${super.packageName}.unit"

    data object Dp: ComposeUiUnit()

}

internal fun FileSpec.Builder.import(
    importable: Importable, nameType: Importable.NameType = Importable.NameType.Class
): FileSpec.Builder = apply {
    addImport(importable.packageName, importable.short(nameType))
}
package me.ks.chan.material.symbols.ksp.ext

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal sealed class Importable {

    sealed class NameType {

        data object Class: NameType()

        data object Method: NameType()

        internal val isClass: Boolean
            get() {
                @OptIn(ExperimentalContracts::class)
                contract { returns(true) implies (this@NameType is Class) }
                return this is Class
            }

        /** Currently, we don't need `isMethod` yet **/
        // internal val isMethod: Boolean
        //     get() {
        //         @OptIn(ExperimentalContracts::class)
        //         contract { returns(true) implies (this@NameType is Method) }
        //         return this is Method
        //     }

    }

    abstract val packageName: String

    protected open val classname: String by lazy { this::class.simpleName!! }

    fun short(nameType: NameType = NameType.Class): String = classname.let {
        it.takeIf { nameType.isClass } ?: it.replaceFirstChar(Char::lowercaseChar)
    }

    fun full(nameType: NameType = NameType.Class): String {
        return "${packageName}.${short(nameType)}"
    }

    fun className(nameType: NameType = NameType.Class): ClassName {
        return ClassName(packageName, short(nameType))
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

internal sealed class Compose: Importable() {
    override val packageName: String
        get() = "androidx.compose"
}

internal sealed class ComposeMaterial3: Compose() {

    override val packageName: String
        get() = "${super.packageName}.material3"

    data object Icon: ComposeMaterial3()

}

internal sealed class ComposeRuntime: Compose() {

    override val packageName: String
        get() = "${super.packageName}.runtime"

    data object Composable: ComposeRuntime()

}

internal sealed class ComposeUi: Compose() {

    override val packageName: String
        get() = "${super.packageName}.ui"

}

internal sealed class ComposeUiGraphics: ComposeUi() {

    override val packageName: String
        get() = "${super.packageName}.graphics"

    data object Color: ComposeUiGraphics()
    data object SolidColor: ComposeUiGraphics()
    data object StrokeJoin: ComposeUiGraphics()

}

internal sealed class ComposeUiTooling: ComposeUi() {
    override val packageName: String
        get() = "${super.packageName}.tooling"
}

internal sealed class ComposeUiToolingPreview: ComposeUiTooling() {

    override val packageName: String
        get() = "${super.packageName}.preview"

    data object Preview: ComposeUiToolingPreview()

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
    importable: Importable,
    nameType: Importable.NameType = Importable.NameType.Class
): FileSpec.Builder = addImport(importable.packageName, importable.short(nameType))

internal fun FileSpec.Builder.predicateImport(
    importable: Importable,
    nameType: Importable.NameType = Importable.NameType.Class,
    predicate: () -> Boolean
): FileSpec.Builder = apply {
    if (predicate()) {
        addImport(importable.packageName, importable.short(nameType))
    }
}
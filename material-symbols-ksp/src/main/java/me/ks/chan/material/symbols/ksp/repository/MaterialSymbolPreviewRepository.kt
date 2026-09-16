package me.ks.chan.material.symbols.ksp.repository

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.toClassName
import me.ks.chan.material.symbols.ksp.ext.ComposeMaterial3
import me.ks.chan.material.symbols.ksp.ext.ComposeRuntime
import me.ks.chan.material.symbols.ksp.ext.ComposeUiToolingPreview
import me.ks.chan.material.symbols.ksp.ext.Importable

class MaterialSymbolPreviewRepository(
    classDeclaration: KSClassDeclaration
) {

    private val supertype = "${classDeclaration.toClassName().simpleName}Impl"

    private val propertyDeclarationList = mutableListOf<KSPropertyDeclaration>()

    operator fun plusAssign(propertyDeclaration: KSPropertyDeclaration) {
        propertyDeclarationList += propertyDeclaration
    }

    val asFunSpecList: List<FunSpec>
        get() = propertyDeclarationList.map { propertyDeclaration ->
            FunSpec.builder(name = "${propertyDeclaration.simpleName.asString()}Preview")
                // @Preview
                .addAnnotation(ComposeUiToolingPreview.Preview.className())
                // @Composable
                .addAnnotation(ComposeRuntime.Composable.className())
                .addModifiers(KModifier.PRIVATE)
                .addStatement(
                    format = "${ComposeMaterial3.Icon.short(name = Importable.NameType.Class)}(imageVector = %L, contentDescription = %L)",
                    args = arrayOf(
                        /*imageVector = */"${supertype}.${propertyDeclaration.simpleName.asString()}",
                        /*contentDescription = */null
                    )
                )
                .build()
        }

}
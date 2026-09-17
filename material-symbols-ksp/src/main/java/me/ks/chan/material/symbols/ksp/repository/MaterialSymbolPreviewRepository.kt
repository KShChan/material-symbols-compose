package me.ks.chan.material.symbols.ksp.repository

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import me.ks.chan.material.symbols.ksp.ext.ComposeMaterial3
import me.ks.chan.material.symbols.ksp.ext.ComposeRuntime
import me.ks.chan.material.symbols.ksp.ext.ComposeUiToolingPreview

class MaterialSymbolPreviewRepository(classDeclaration: KSClassDeclaration) {

    private val classname: String = classDeclaration.simpleName.asString()

    operator fun invoke(propertyDeclaration: KSPropertyDeclaration): FunSpec {
        val property = propertyDeclaration.simpleName.asString()

        return FunSpec.builder(name = "${classname}${property}Preview")
            // @Preview
            .addAnnotation(ComposeUiToolingPreview.Preview.className())
            // @Composable
            .addAnnotation(ComposeRuntime.Composable.className())
            .addModifiers(KModifier.PRIVATE)
            .addStatement(
                format = "${ComposeMaterial3.Icon.short()}(imageVector = %L.%N, contentDescription = %L)",
                args = arrayOf(
                    /*imageVector = */"${classname}Impl", property,
                    /*contentDescription = */null
                )
            )
            .build()
    }

}
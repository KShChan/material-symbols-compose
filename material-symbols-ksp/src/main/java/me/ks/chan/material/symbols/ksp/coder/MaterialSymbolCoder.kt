package me.ks.chan.material.symbols.ksp.coder

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.addOriginatingKSFile
import com.squareup.kotlinpoet.ksp.toClassName
import me.ks.chan.material.symbols.ksp.ext.ComposeMaterial3
import me.ks.chan.material.symbols.ksp.ext.ComposeUiVectorGraphics
import me.ks.chan.material.symbols.ksp.ext.Importable
import me.ks.chan.material.symbols.ksp.ext.MaterialSymbols
import me.ks.chan.material.symbols.ksp.ext.import
import me.ks.chan.material.symbols.ksp.ext.predicateImport

class MaterialSymbolCoder(
    private val classDeclaration: KSClassDeclaration,
    private val iconStyledPropertySpecList: List<PropertySpec>,
    private val previewIconFunSpecList: List<FunSpec>
): Coder {

    override val dependencies: Dependencies
        get() = Dependencies(aggregating = true, classDeclaration.containingFile!!)

    override val fileSpec: FileSpec
        get() {
            val supertype = classDeclaration.toClassName()
            val classname = supertype.simpleName + "Impl"

            return FileSpec.builder(supertype.packageName, classname)
                .import(ComposeUiVectorGraphics.ImageVector)
                .predicateImport(
                    importable = ComposeMaterial3.Icon,
                    predicate = previewIconFunSpecList::isNotEmpty
                )
                .import(MaterialSymbols)
                .import(MaterialSymbols.MaterialSymbol, Importable.NameType.Method)
                .addType(
                    TypeSpec.objectBuilder(classname)
                        .addOriginatingKSFile(classDeclaration.containingFile!!)
                        // Do not expose impl object, just let it be handled by getter delegate method
                        .addModifiers(KModifier.PRIVATE)
                        .addModifiers(KModifier.DATA)
                        .supertype(classDeclaration, supertype)
                        .addProperties(iconStyledPropertySpecList)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder(supertype.simpleName, supertype)
                        .receiver(MaterialSymbols.className())
                        .getter(
                            FunSpec.getterBuilder()
                                .addStatement(
                                    "return %T", ClassName(supertype.packageName, classname)
                                )
                                .build()
                        )
                        .build()
                )
                .addFunctions(previewIconFunSpecList)
                .build()
        }

}

private fun TypeSpec.Builder.supertype(
    classDeclaration: KSClassDeclaration, className: ClassName,
): TypeSpec.Builder = when {
    classDeclaration.classKind == ClassKind.INTERFACE -> { addSuperinterface(className) }
    else -> { superclass(className) }
}
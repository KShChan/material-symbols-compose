package me.ks.chan.material.symbols.ksp.validator

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.isProtected
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.ksp.toClassName
import me.ks.chan.material.symbols.annotation.PreviewIcon
import me.ks.chan.material.symbols.annotation.SkipPreview
import me.ks.chan.material.symbols.annotation.Style
import me.ks.chan.material.symbols.ksp.ext.ComposeUiVectorGraphics
import me.ks.chan.material.symbols.ksp.ext.annotationExists

class PropertyValidator(kspLogger: KSPLogger):
    KSDefaultVisitor<Boolean, PropertyValidator.Result>(),
    KSPLogger by kspLogger {

    override fun defaultHandler(node: KSNode, data: Boolean): Result =
        throw IllegalAccessError()

    enum class Result { Valid, Filter, Error }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Boolean): Result {
        val isAbstractProperty = property.isAbstract()
        val isValidPropertyType = property.type.resolve().toClassName() ==
            ComposeUiVectorGraphics.ImageVector.className()
        val isAnnotatedProperty = property.annotationExists<Style>()
        val isOpenProperty = property.isOpen()
        val isPreviewIcon = property.annotationExists<PreviewIcon>()
        val isSkipPreview = property.annotationExists<SkipPreview>()
        val isProtected = property.isProtected()

        return when {
            // Abstract property with invalid type
            isAbstractProperty && !isValidPropertyType -> {
                property.invalidAbstractPropertyError()
                Result.Error
            }
            isAbstractProperty && !isAnnotatedProperty -> {
                property.noStyleAnnotatedPropertyError()
                Result.Error
            }
            // Not targeted property
            !isAbstractProperty && !isAnnotatedProperty -> {
                // No need to be processed and logged
                Result.Filter
            }
            !isOpenProperty && isValidPropertyType -> {
                property.finalPropertyInfo()
                Result.Filter
            }
            // Filter implemented property with @Style annotated
            !isAbstractProperty && isOpenProperty && isValidPropertyType -> {
                property.overriddenPropertyWarning()
                Result.Filter
            }
            isPreviewIcon && isSkipPreview -> {
                property.previewIconAndSkipMutuallyExclusiveError()
                Result.Error
            }
            isProtected && data && !isSkipPreview -> {
                property.previewIconInvalidAccessError()
                Result.Error
            }
            else -> { Result.Valid }
        }
    }

}

context(kspLogger: KSPLogger)
private fun KSPropertyDeclaration.invalidAbstractPropertyError() {
    kspLogger.error(
        message = "MaterialSymbol member abstract property must be typed as ${ComposeUiVectorGraphics.ImageVector.full()}.",
        symbol = this
    )
}

context(kspLogger: KSPLogger)
private fun KSPropertyDeclaration.noStyleAnnotatedPropertyError() {
    kspLogger.error(
        message = "MaterialSymbol class member abstract property should be annotated with @Style.",
        symbol = this
    )
}

context(kspLogger: KSPLogger)
private fun KSPropertyDeclaration.finalPropertyInfo() {
    kspLogger.info(
        message = "MaterialSymbol class member final property will be skipped.",
        symbol = this
    )
}

context(kspLogger: KSPLogger)
private fun KSPropertyDeclaration.overriddenPropertyWarning() {
    kspLogger.warn(
        message = "MaterialSymbol member implemented property will be skipped.",
        symbol = this
    )
}

context(kspLogger: KSPLogger)
private fun KSPropertyDeclaration.previewIconAndSkipMutuallyExclusiveError() {
    kspLogger.error(
        message = "PreviewIcon and SkipPreview annotations are mutually exclusive with each other.",
        symbol = this
    )
}

context(kspLogger: KSPLogger)
private fun KSPropertyDeclaration.previewIconInvalidAccessError() {
    kspLogger.error(
        message = "PreviewIcon should not be protected",
        symbol = this
    )
}
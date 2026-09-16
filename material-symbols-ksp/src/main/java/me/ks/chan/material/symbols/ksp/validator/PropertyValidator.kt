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

class PropertyValidator(private val kspLogger: KSPLogger): KSDefaultVisitor<Boolean, PropertyValidator.Result>() {

    override fun defaultHandler(node: KSNode, data: Boolean): Result =
        throw IllegalAccessError()

    enum class Result { Valid, Filter, Error }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Boolean): Result {
        val isAbstractProperty = property.isAbstract()
        val isValidPropertyType = property.type.resolve().toClassName() ==
            ComposeUiVectorGraphics.ImageVector.className()
        @OptIn(KspExperimental::class)
        val isAnnotatedProperty = property.isAnnotationPresent(Style::class)
        val isOpenProperty = property.isOpen()
        val isPreviewIcon = property.annotationExists<PreviewIcon>()
        val isSkipPreview = property.annotationExists<SkipPreview>()
        val isProtected = property.isProtected()

        return when {
            // Abstract property with invalid type
            isAbstractProperty && !isValidPropertyType -> {
                kspLogger.invalidAbstractPropertyError(property)
                Result.Error
            }
            isAbstractProperty && !isAnnotatedProperty -> {
                kspLogger.noStyleAnnotatedPropertyError(property)
                Result.Error
            }
            // Not targeted property
            !isAbstractProperty && !isAnnotatedProperty -> {
                // No need to be processed and logged
                Result.Filter
            }
            !isOpenProperty && isValidPropertyType -> {
                kspLogger.finalPropertyInfo(property)
                Result.Filter
            }
            // Filter implemented property with @Style annotated
            !isAbstractProperty && isOpenProperty && isValidPropertyType -> {
                kspLogger.overriddenPropertyWarning(property)
                Result.Filter
            }
            isPreviewIcon && isSkipPreview -> {
                kspLogger.previewIconAndSkipMutuallyExclusiveError(property)
                Result.Error
            }
            isProtected && data && !isSkipPreview -> {
                kspLogger.previewIconInvalidAccessError(property)
                Result.Error
            }
            else -> { Result.Valid }
        }
    }

}

private fun KSPLogger.invalidAbstractPropertyError(propertyDeclaration: KSPropertyDeclaration) {
    val imageVector = ComposeUiVectorGraphics.ImageVector.full()
    error(
        message = "MaterialSymbol member abstract property must be typed as ${imageVector}.",
        symbol = propertyDeclaration
    )
}

private fun KSPLogger.noStyleAnnotatedPropertyError(propertyDeclaration: KSPropertyDeclaration) {
    error(
        message = "MaterialSymbol class member abstract property should be annotated with @Style.",
        symbol = propertyDeclaration
    )
}

private fun KSPLogger.finalPropertyInfo(propertyDeclaration: KSPropertyDeclaration) {
    info(
        message = "MaterialSymbol class member final property will be skipped.",
        symbol = propertyDeclaration
    )
}

private fun KSPLogger.overriddenPropertyWarning(propertyDeclaration: KSPropertyDeclaration) {
    warn(
        message = "MaterialSymbol member implemented property will be skipped.",
        symbol = propertyDeclaration
    )
}

private fun KSPLogger.previewIconAndSkipMutuallyExclusiveError(propertyDeclaration: KSPropertyDeclaration) {
    error(
        message = "PreviewIcon and SkipPreview annotations are mutually exclusive with each other.",
        symbol = propertyDeclaration
    )
}

private fun KSPLogger.previewIconInvalidAccessError(propertyDeclaration: KSPropertyDeclaration) {
    error(
        message = "PreviewIcon should not be protected",
        symbol = propertyDeclaration
    )
}
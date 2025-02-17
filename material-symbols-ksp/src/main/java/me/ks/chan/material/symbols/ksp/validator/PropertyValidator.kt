package me.ks.chan.material.symbols.ksp.validator

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.ksp.toClassName
import me.ks.chan.material.symbols.annotation.Style
import me.ks.chan.material.symbols.ksp.ext.ComposeUiVectorGraphics

class PropertyValidator(
    private val kspLogger: KSPLogger,
): KSDefaultVisitor<Unit, PropertyValidator.Result>() {

    override fun defaultHandler(node: KSNode, data: Unit): Result =
        throw IllegalAccessError()

    enum class Result { Valid, Filter, Error }

    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit): Result {
        val isAbstractProperty = property.isAbstract()
        val isValidPropertyType = property.type.resolve().toClassName() ==
            ComposeUiVectorGraphics.ImageVector.className()
        @OptIn(KspExperimental::class)
        val isAnnotatedProperty = property.isAnnotationPresent(Style::class)
        val isOpenProperty = property.isOpen()

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
            else -> { Result.Valid }
        }
    }

}

private fun KSPLogger.invalidAbstractPropertyError(
    propertyDeclaration: KSPropertyDeclaration
) {
    val imageVector = ComposeUiVectorGraphics.ImageVector.full()
    error(
        message = "MaterialSymbol member abstract property must be typed as ${imageVector}.",
        propertyDeclaration,
    )
}

private fun KSPLogger.noStyleAnnotatedPropertyError(
    propertyDeclaration: KSPropertyDeclaration
) {
    error(
        message = "MaterialSymbol class member abstract property should be annotated with @Style.",
        propertyDeclaration,
    )
}

private fun KSPLogger.finalPropertyInfo(
    propertyDeclaration: KSPropertyDeclaration
) {
    info(
        message = "MaterialSymbol class member final property will be skipped.",
        propertyDeclaration,
    )
}

private fun KSPLogger.overriddenPropertyWarning(
    propertyDeclaration: KSPropertyDeclaration
) {
    warn(
        message = "MaterialSymbol member implemented property will be skipped.",
        propertyDeclaration,
    )
}
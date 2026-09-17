package me.ks.chan.material.symbols.ksp.validator

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isOpen
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import me.ks.chan.material.symbols.annotation.PreviewIcon
import me.ks.chan.material.symbols.ksp.ext.annotationExists
import me.ks.chan.material.symbols.ksp.validator.ClassValidator.Result.Error
import me.ks.chan.material.symbols.ksp.validator.ClassValidator.Result.Filter
import me.ks.chan.material.symbols.ksp.validator.ClassValidator.Result.Pass

class ClassValidator(kspLogger: KSPLogger):
    KSDefaultVisitor<Unit, ClassValidator.Result>(),
    KSPLogger by kspLogger {

    sealed class Result(val classDeclaration: KSClassDeclaration) {

        class Pass(classDeclaration: KSClassDeclaration): Result(classDeclaration)

        class Filter(classDeclaration: KSClassDeclaration): Result(classDeclaration)

        class Error(classDeclaration: KSClassDeclaration): Result(classDeclaration)

    }

    private val propertyValidator = PropertyValidator(kspLogger)

    override fun defaultHandler(node: KSNode, data: Unit): Result {
        throw UnsupportedOperationException()
    }

    override fun visitClassDeclaration(
        classDeclaration: KSClassDeclaration, data: Unit
    ): Result = when {
        classDeclaration.getDeclaredFunctions().any(KSFunctionDeclaration::isAbstract) -> {
            classDeclaration.abstractFunctionError()
            classDeclaration.errorResult
        }
        classDeclaration.isOpen().not() -> {
            classDeclaration.nonOpenClassInfo()
            classDeclaration.filterResult
        }
        else -> {
            val isPreviewIcon = classDeclaration.annotationExists<PreviewIcon>()
            val propertyValidationResultList = classDeclaration.getDeclaredProperties()
                .map { propertyDeclaration -> propertyDeclaration.accept(propertyValidator, isPreviewIcon) }

            when {
                propertyValidationResultList.any { it == PropertyValidator.Result.Error } -> {
                    // Detail content is logged in PropertyValidator
                    classDeclaration.errorResult
                }
                propertyValidationResultList.none { it == PropertyValidator.Result.Valid } -> {
                    classDeclaration.noneOverridablePropertyInfo()
                    classDeclaration.filterResult
                }
                else -> { classDeclaration.passResult }
            }
        }
    }

}

private inline val KSClassDeclaration.passResult: Pass
    get() = Pass(this)

private inline val KSClassDeclaration.filterResult: Filter
    get() = Filter(this)

private inline val KSClassDeclaration.errorResult: Error
    get() = Error(this)

context(kspLogger: KSPLogger)
private fun KSClassDeclaration.abstractFunctionError() {
    kspLogger.error(
        message = "MaterialSymbol class should not contain any abstract function.",
        symbol = this
    )
}

context(kspLogger: KSPLogger)
private fun KSClassDeclaration.nonOpenClassInfo() {
    kspLogger.info(
        message = "MaterialSymbol class should be declaration as open/abstract class or interface.",
        symbol = this
    )
}

context(kspLogger: KSPLogger)
private fun KSClassDeclaration.noneOverridablePropertyInfo() {
    kspLogger.info(
        message = "MaterialSymbol class has not declared overridable property.",
        symbol = this
    )
}

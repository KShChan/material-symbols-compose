package me.ks.chan.material.symbols.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import me.ks.chan.material.symbols.annotation.MaterialSymbol
import me.ks.chan.material.symbols.ksp.coder.MaterialSymbolsCoder
import me.ks.chan.material.symbols.ksp.coder.starts
import me.ks.chan.material.symbols.ksp.validator.ClassValidator
import me.ks.chan.material.symbols.ksp.visitor.MaterialSymbolClassVisitor
import okhttp3.OkHttpClient

class MaterialSymbolsSymbolProcessor(
    codeGenerator: CodeGenerator,
    kspLogger: KSPLogger,
    okHttpClient: OkHttpClient,
): SymbolProcessor {

    init {
        codeGenerator starts MaterialSymbolsCoder
    }

    private val classValidator by lazy { ClassValidator(kspLogger) }
    private val materialSymbolClassVisitor by lazy {
        MaterialSymbolClassVisitor(kspLogger, codeGenerator, okHttpClient)
    }

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val classValidationResultList = resolver.getSymbolsWithAnnotation(MaterialSymbol::class.qualifiedName!!)
            .mapNotNull { ksAnnotated ->
                when (ksAnnotated) {
                    is KSClassDeclaration -> { ksAnnotated.accept(classValidator, Unit) }
                    else -> { null }
                }
            }
            .toList()

        val validClassList = classValidationResultList.filterIsInstance<ClassValidator.Result.Pass>()
            .toSet()
            .onEach { it.classDeclaration.accept(materialSymbolClassVisitor, Unit) }

        return (classValidationResultList - validClassList)
            .map(ClassValidator.Result::classDeclaration)
    }

}
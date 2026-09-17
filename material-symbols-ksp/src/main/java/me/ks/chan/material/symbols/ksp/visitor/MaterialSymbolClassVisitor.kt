package me.ks.chan.material.symbols.ksp.visitor

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ksp.toClassName
import me.ks.chan.material.symbols.annotation.Filled
import me.ks.chan.material.symbols.annotation.Grade
import me.ks.chan.material.symbols.annotation.MaterialSymbol
import me.ks.chan.material.symbols.annotation.OpticalSize
import me.ks.chan.material.symbols.annotation.PreviewIcon
import me.ks.chan.material.symbols.annotation.SkipPreview
import me.ks.chan.material.symbols.annotation.Style
import me.ks.chan.material.symbols.annotation.Weight
import me.ks.chan.material.symbols.ksp.annotation.MaterialSymbolIcon
import me.ks.chan.material.symbols.ksp.coder.MaterialSymbolCoder
import me.ks.chan.material.symbols.ksp.coder.starts
import me.ks.chan.material.symbols.ksp.ext.annotation
import me.ks.chan.material.symbols.ksp.ext.annotationExists
import me.ks.chan.material.symbols.ksp.ext.annotationOrNull
import me.ks.chan.material.symbols.ksp.ext.asSnackCase
import me.ks.chan.material.symbols.ksp.repository.MaterialDesignIconsRepository
import me.ks.chan.material.symbols.ksp.repository.MaterialSymbolPreviewRepository
import me.ks.chan.material.symbols.ksp.repository.MaterialSymbolsPropertyRepository
import me.ks.chan.material.symbols.ksp.repository.MaterialSymbolsRepository
import me.ks.chan.material.symbols.ksp.repository.PathBuilderRepository
import me.ks.chan.material.symbols.ksp.repository.VectorDrawableRepository
import me.ks.chan.material.symbols.ksp.repository.processWith
import okhttp3.OkHttpClient

class MaterialSymbolClassVisitor(
    kspLogger: KSPLogger, private val codeGenerator: CodeGenerator, okHttpClient: OkHttpClient
): KSVisitorVoid() {

    private val buildIconUrlWith: String.(MaterialSymbolIcon) -> String = MaterialDesignIconsRepository(kspLogger)::invoke
    private val requestMaterialSymbol: String.() -> String = MaterialSymbolsRepository(okHttpClient, kspLogger)::invoke

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val previewAllIcons = classDeclaration.annotationExists<PreviewIcon>()

        val toPreviewIconFunSpec: KSPropertyDeclaration.() -> FunSpec =
            MaterialSymbolPreviewRepository(classDeclaration)::invoke

        val iconName by classDeclaration.materialSymbolName
        val pairedSpecList = classDeclaration.getDeclaredProperties()
            /**
             * Use single method (i.e., [mapNotNullTo]) to do both
             * (1) Filtering out abstract properties with @Style annotation; and
             * (2) Map to property spec list
             * with lesser loops and better readability.
             **/
            .mapNotNullTo(ArrayList()) { propertyDeclaration ->
                // Filter out abstract properties with @Style annotation
                propertyDeclaration.takeIf(KSPropertyDeclaration::isStyleAnnotatedAbstractProperty)
                    ?.let {
                        // Map to property spec list
                        val materialSymbolIcon = propertyDeclaration.asMaterialSymbolIcon

                        val propertySpec = iconName.buildIconUrlWith(materialSymbolIcon)
                            .requestMaterialSymbol()
                            .processWith(VectorDrawableRepository)
                            .processWith(PathBuilderRepository)
                            .processWith(
                                MaterialSymbolsPropertyRepository(propertyDeclaration, materialSymbolIcon)
                            )

                        val isSkipPreview = propertyDeclaration.annotationExists<SkipPreview>()
                        val isPreviewIcon = propertyDeclaration.annotationExists<PreviewIcon>()
                        val funSpec = when {
                            previewAllIcons && !isSkipPreview || isPreviewIcon -> {
                                propertyDeclaration.toPreviewIconFunSpec()
                            }
                            else -> { null }
                        }

                        propertySpec to funSpec
                    }
            }

        val (propertySpec, funSpec) = pairedSpecList.unzip()
        codeGenerator starts MaterialSymbolCoder(classDeclaration, propertySpec, funSpec.filterNotNull())
    }

}

private inline val KSClassDeclaration.materialSymbolName: Lazy<String>
    get() = lazy {
        annotation(MaterialSymbol::name).takeIf(String::isNotBlank) ?:
            toClassName().simpleName.asSnackCase
    }

private inline val KSPropertyDeclaration.isStyleAnnotatedAbstractProperty: Boolean
    get() = annotationExists<Style>() && isAbstract()

private val KSPropertyDeclaration.asMaterialSymbolIcon: MaterialSymbolIcon
    get() = MaterialSymbolIcon(
        style = annotation(Style::value),
        weight = annotationOrNull(Weight::value),
        grade = annotationOrNull(Grade::value),
        filled = annotationExists<Filled>(),
        opticalSize = annotationOrNull(OpticalSize::value),
    )

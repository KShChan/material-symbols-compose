package me.ks.chan.material.symbols.example.icon

import androidx.compose.ui.graphics.vector.ImageVector
import me.ks.chan.material.symbols.annotation.Filled
import me.ks.chan.material.symbols.annotation.Grade
import me.ks.chan.material.symbols.annotation.OpticalSize
import me.ks.chan.material.symbols.annotation.Weight
import me.ks.chan.material.symbols.annotation.MaterialSymbol
import me.ks.chan.material.symbols.annotation.MaterialSymbolStyle
import me.ks.chan.material.symbols.annotation.PreviewIcon
import me.ks.chan.material.symbols.annotation.Style

/** 1. Annotate @MaterialSymbol **/
@MaterialSymbol
/** 2. Annotate @PreviewIcon **/
/** 3. Define `interface` **/
interface CategorySearch {

    /** Specific icon style preview composable method generating, other non-annotated will not be generated **/
    @PreviewIcon
    /** 5. Annotate @Style with parameter filled with [MaterialSymbolStyle] **/
    @Style(MaterialSymbolStyle.Rounded)
    /** 6. Define `abstract` property (without content implementation) **/
    val rounded: ImageVector    /** 5. Define property type as [ImageVector] **/

    /** Other same as well. **/
    @Style(MaterialSymbolStyle.Rounded)
    /** You can add customization to icon **/
    @Filled
    /**
     * Other customization specifications are also available.
     * Annotate with the customization if required.
     * (1) @[Weight], (2) @[Grade], (3) @[Filled], (4) @[OpticalSize]
     **/
    val filled: ImageVector

}
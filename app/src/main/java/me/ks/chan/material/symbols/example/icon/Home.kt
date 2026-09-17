package me.ks.chan.material.symbols.example.icon

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import me.ks.chan.material.symbols.MaterialSymbols
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
/** 2. Annotate @MaterialSymbol **/
@PreviewIcon
/** 3. Define `interface` **/
interface Home {

    @Suppress("PropertyName")
    /** 4. Annotate @Style with parameter filled with [MaterialSymbolStyle] **/
    @Style(MaterialSymbolStyle.Rounded)
    /** 5. Define `abstract` property (without content implementation) **/
    val Rounded: ImageVector    /** 5. Define property type as [ImageVector] **/

    @Suppress("PropertyName")
    /** Other same as well. **/
    @Style(MaterialSymbolStyle.Rounded)
    /** You can add customization to icon **/
    @Filled
    /**
     * Other customization specifications are also available.
     * Annotate with the customization if required.
     * (1) @[Weight], (2) @[Grade], (3) @[Filled], (4) @[OpticalSize]
     **/
    val Filled: ImageVector

    /**
     * CAUTION:
     * Other property or should not be abstract (without implementation)
     * It is strongly recommended not to add any members other than defining [Style]
     **/
    @Composable
    fun RoundedIcon(contentDescription: String?) {
        Icon(
            imageVector = Rounded,
            contentDescription = contentDescription
        )
    }

    @Composable
    fun FilledRoundedIcon(contentDescription: String?) {
        Icon(
            imageVector = Filled,
            contentDescription = contentDescription
        )
    }

}

/** Let's preview **/
@Composable
@Preview
private fun Preview() {
    Column {
        FilledTonalIconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = MaterialSymbols.Home.Rounded, contentDescription = null)
        }

        FilledIconButton(onClick = { /*TODO*/ }) {
            MaterialSymbols.Home.FilledRoundedIcon(contentDescription = null)
        }

        val interactionSource = remember { MutableInteractionSource() }
        val isPressing by interactionSource.collectIsPressedAsState()
        FilledIconToggleButton(
            checked = isPressing,
            interactionSource = interactionSource,
            onCheckedChange = {},
        ) {
            when {
                isPressing -> {
                    MaterialSymbols.Home.FilledRoundedIcon(contentDescription = null)
                }
                else -> {
                    MaterialSymbols.Home.RoundedIcon(contentDescription = null)
                }
            }
        }
    }
}
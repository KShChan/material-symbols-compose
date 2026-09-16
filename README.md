# MSc: material-symbols-compose

[![](https://jitpack.io/v/KShChan/material-symbols-compose.svg)](https://jitpack.io/#KShChan/material-symbols-compose)

## Install

1. Install KSP
   - Have a look at [KSP quickstart - Add a processor](https://kotlinlang.org/docs/ksp-quickstart.html#add-a-processor)
      ```kotlin
      plugins {
          id("com.google.devtools.ksp") version "<KSP_VERSION>"
          id("org.jetbrains.kotlin.android") version "<KOTLIN_VERSION>"
      }
      ```    

2. Add Jitpack maven repository url
   - At `settings.gradle.kts`
      ```kotlin
      dependencyResolutionManagement {
          repositories {
              // ...
              // Add following line
              maven { url = uri("https://jitpack.io") }
          }
      }
      ```
      
3. Add MSC annotation and KSP dependencies
   - At app module `build.gradle.kts` dependencies block
      ```kotlin
      dependencies {
          // ...
          val materialSymbolsCompose = "<MSC_VERSION>"
          implementation("com.github.KShChan.material-symbols-compose:annotation:$materialSymbolsCompose")
          ksp("com.github.KShChan.material-symbols-compose:ksp:$materialSymbolsCompose")
          // ...
      }
      ```

## Usage

### General Usage

[MSc](https://github.com/KShChan/material-symbols-compose) supports declaring symbols in both interface and abstract class.

```kotlin
// import me.ks.chan.material.symbols.annotation.MaterialSymbol
// import me.ks.chan.material.symbols.annotation.MaterialSymbolStyle
// import me.ks.chan.material.symbols.annotation.PreviewIcon
// import me.ks.chan.material.symbols.annotation.SkipPreview
// import me.ks.chan.material.symbols.annotation.Style
// import me.ks.chan.material.symbols.annotation.Filled

/**
 * For icon containing numeric characters, for example `Exposure Plus 1`, make sure to define as parameter of annotation
 * If your icon does not contain numeric characters, you may directly use pascal case to default classname
 **/
@MaterialSymbol("exposure_plus_1")
/**
 * [PreviewIcon] annotated at class-level will generate all [Style] annotated unless [SkipPreview] 
 * is declared specially for specific [Style] annotated delegate.
 * You can also declare [PreviewIcon] for specific [Style] annotated delegate so that declared
 * will be generated specifically
 **/
@PreviewIcon
@MaterialSymbol
interface Home {
    
    @Suppress("PropertyName")
    @Style(MaterialSymbolStyle.Rounded)
    val Rounded: ImageVector
    
    @SkipPreview    // If you don't want to generate preview composable function for this
    @Suppress("PropertyName")
    @Style(MaterialSymbolStyle.Rounded)
    @Filled
    val Filled: ImageVector
    
}
```
Then, build the icon with clicking Gradle panel `Gradle->Tasks->other->kspDebugKotlin`
```kotlin
// import me.ks.chan.material.symbols.MaterialSymbols
@Composable
@Preview
private fun Preview() {
    Column {
        FilledTonalIconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = MaterialSymbols.Home.Rounded, contentDescription = "Rounded Home")
        }

        FilledIconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = MaterialSymbols.Home.Filled, contentDescription = "Filled Rounded Home")
        }

        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()
        val (icon, contentDescription) by remember {
            derivedStateOf {
                when {
                    isPressed -> { MaterialSymbols.Home.Filled to "Pressed: Filled Rounded Home" }
                    else -> { MaterialSymbols.Home.Rounded to "Released Rounded Home" }
                }
            }
        }
        FilledIconToggleButton(
            checked = isPressed,
            interactionSource = interactionSource,
            onCheckedChange = {},
        ) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    }
}
```
![](img/img_col_home_buttons.png)

For more detail, you can refer to [interface](app/src/main/java/me/ks/chan/material/symbols/example/icon/Home.kt) 
or [abstract class](app/src/main/java/me/ks/chan/material/symbols/example/icon/Settings.kt) 
implementation examples.

Please read the rules of annotating [@MaterialSymbol](material-symbols-annotation/src/main/kotlin/me/ks/chan/material/symbols/annotation/MaterialSymbol.kt).

### Customization

You can customize the generated symbol by adding [customization annotations](material-symbols-annotation/src/main/kotlin/me/ks/chan/material/symbols/annotation/MaterialSymbolCustomize.kt)
to the symbol declaration.

## License 

### [Apache License 2.0](LICENSE)

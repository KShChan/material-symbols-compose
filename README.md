# MSc: material-symbols-compose

[![](https://jitpack.io/v/1552980358/material-symbols-compose.svg)](https://jitpack.io/#1552980358/material-symbols-compose)

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
          implementation("com.github.1552980358.material-symbols-compose:annotation:$materialSymbolsCompose")
          ksp("com.github.1552980358.material-symbols-compose:ksp:$materialSymbolsCompose")
          // ...
      }
      ```

## Usage

### General Usage

[MSc](https://github.com/1552980358/material-symbols-compose) supports declaring symbols in both interface and abstract class.

```kotlin
// import me.ks.chan.material.symbols.annotation.MaterialSymbol
// import me.ks.chan.material.symbols.annotation.MaterialSymbolStyle
// import me.ks.chan.material.symbols.annotation.Style
// import me.ks.chan.material.symbols.annotation.Filled
// For icon containing numeric characters, for example `Exposure Plus 1`, make sure to define as parameter of annotation
// @MaterialSymbol("exposure_plus_1")
// If your icon does not contain numeric characters, you may directly use pascal case to default classname
@Suppress("PropertyName")
@MaterialSymbol
interface Home {
    @Style(MaterialSymbolStyle.Rounded)
    val Rounded: ImageVector

    @Style(MaterialSymbolStyle.Rounded)
    @Filled
    val filled: ImageVector
}
```
Then, build the icon with clicking Gradle panel `Gradle->Tasks->otherkspDebugKotlin`
```kotlin
// import me.ks.chan.material.symbols.MaterialSymbols
@Composable
@Preview
private fun Preview() {
    Column {
        FilledTonalIconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = MaterialSymbols.Home.rounded, contentDescription = null)
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
```
![](img/img_col_home_buttons.png)

For more detail, you can refers to [interface](app/src/main/java/me/ks/chan/material/symbols/example/icon/Home.kt) 
or [abstract class](app/src/main/java/me/ks/chan/material/symbols/example/icon/Settings.kt) 
implementation examples.

Please read the rules of annotating [@MaterialSymbol](material-symbols-annotation/src/main/kotlin/me/ks/chan/material/symbols/annotation/MaterialSymbol.kt).

### Customization

You can customize the generated symbol by adding [customization annotations](material-symbols-annotation/src/main/kotlin/me/ks/chan/material/symbols/annotation/MaterialSymbolCustomize.kt)
to the symbol declaration.

## License 

### [Apache License 2.0](LICENSE)

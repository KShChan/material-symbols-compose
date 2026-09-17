package me.ks.chan.material.symbols.annotation

/**
 * This [PreviewIcon] requests for generating preview composable function(s) for icon(s) in
 * corresponding implementation `.kt` file right below data object impl getter delegate.
 * The implementation file can be jumped to by `Shift + RightClick` or `MiddleClick` with
 * mouse pointer pointing to [MaterialSymbol] annotated interface or abstract and search for  file
 * with suffix `Impl.kt`. The preview composable function(s) will be generated at the bottom part,
 * and can be previewed by switching to `Split` or `Design` mode by clicking right-top mode
 * switching button.
 *
 * The [PreviewIcon] allows being annotated at:
 * 1. Class-level; or
 * 2. Class member level (property & field)
 *
 * #### At class-member-level
 * If [PreviewIcon] is annotated at member level with [Style] annotated,
 * a file-level private preview composable function will be generated.
 * ```
 * import androidx.compose.ui.graphics.vector.ImageVector
 * import me.ks.chan.material.symbols.annotation.Filled
 * import me.ks.chan.material.symbols.annotation.MaterialSymbol
 * import me.ks.chan.material.symbols.annotation.MaterialSymbolStyle
 * import me.ks.chan.material.symbols.annotation.Style
 * import me.ks.chan.material.symbols.annotation.PreviewIcon
 *
 * @MaterialSymbol
 * interface Home {
 *
 *     @PreviewIcon
 *     @Style(MaterialSymbolStyle.Rounded)
 *     val Rounded: ImageVector     // Preview composable function will be generated
 *
 *     @Style(MaterialSymbolStyle.Rounded)
 *     @Filled
 *     val Filled: ImageVector      // Preview composable function will not be generated
 *
 * }
 * ```
 *
 * #### At class-level
 * If [PreviewIcon] is annotated at class level, all members annotated with [Style] for generating
 * icon will be generated along with a file-level private preview composable function unless [SkipPreview]
 * is annotated to skip generating preview composable function.
 * ```
 * import androidx.compose.ui.graphics.vector.ImageVector
 * import me.ks.chan.material.symbols.annotation.Filled
 * import me.ks.chan.material.symbols.annotation.MaterialSymbol
 * import me.ks.chan.material.symbols.annotation.MaterialSymbolStyle
 * import me.ks.chan.material.symbols.annotation.SkipPreview
 * import me.ks.chan.material.symbols.annotation.Style
 * import me.ks.chan.material.symbols.annotation.PreviewIcon
 *
 * @MaterialSymbol
 * @PreviewIcon
 * interface Home {
 *
 *     @Style(MaterialSymbolStyle.Rounded)
 *     val Rounded: ImageVector     // Preview composable function will be generated
 *
 *     @SkipPreview
 *     @Style(MaterialSymbolStyle.Rounded)
 *     @Filled
 *     val Filled: ImageVector      // Preview composable function will not be generated
 *
 * }
 * ```
 **/
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class PreviewIcon

/**
 * This [SkipPreview] allows annotated to class members within [PreviewIcon] annotated class to
 * skip generating preview composable function
 **/
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class SkipPreview
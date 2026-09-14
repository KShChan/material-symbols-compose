package me.ks.chan.material.symbols.ksp.repository

import kotlin.text.MatchResult.Destructured

data object VectorDrawableRepository: ProcessRepository<String, List<String>> {
    override fun process(unprocessed: String): List<String> {
        return unprocessed.lineSequence()
            .pathCommand
            .asPathCommandList
    }
}

private val PathDataAttributeRegex by lazy("^ {6}android:pathData=\"([MQLZ\\d ,.]+)\"/>"::toRegex)
private val VectorPathCommandActionRegex by lazy("(?=[MQLZ])"::toRegex)

private inline val Sequence<String>.pathCommand: String
    get() = firstNotNullOf { PathDataAttributeRegex.find(it)?.destructured }
        .let(Destructured::component1)

private inline val String.asPathCommandList: List<String>
    get() = split(VectorPathCommandActionRegex).filter(String::isNotBlank)
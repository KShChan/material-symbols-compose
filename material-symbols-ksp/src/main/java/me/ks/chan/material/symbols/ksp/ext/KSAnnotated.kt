package me.ks.chan.material.symbols.ksp.ext

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSAnnotated

@OptIn(KspExperimental::class)
inline fun <reified A: Annotation> KSAnnotated.annotation(): A =
    getAnnotationsByType(A::class).single()

inline fun <reified A: Annotation, R> KSAnnotated.annotation(transform: A.() -> R): R =
    annotation<A>().transform()

@OptIn(KspExperimental::class)
inline fun <reified A: Annotation> KSAnnotated.annotationOrNull(): A? =
    getAnnotationsByType(A::class).singleOrNull()

inline fun <reified A: Annotation, R> KSAnnotated.annotationOrNull(transform: A.() -> R): R? =
    annotationOrNull<A>()?.transform()

inline fun <reified A: Annotation> KSAnnotated.annotationExists(): Boolean =
    @OptIn(KspExperimental::class)
    isAnnotationPresent(A::class)
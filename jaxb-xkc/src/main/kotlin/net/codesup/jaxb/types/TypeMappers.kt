package net.codesup.jaxb.types

import com.sun.codemodel.*
import net.codesup.emit.ConstructorInv
import net.codesup.emit.Expression
import net.codesup.emit.qn
import net.codesup.emit.use.AnnotationUse
import net.codesup.emit.use.ClassTypeUse
import net.codesup.emit.use.KClassUse

/**
 * @author Mirko Klemm 2021-03-24
 *
 */

val typeMappings = listOf(
    "byte" to "kotlin.Byte",
    "short" to "kotlin.Short",
    "int" to "kotlin.Int",
    "long" to "kotlin.Long",
    "char" to "kotlin.Char",
    "float" to "kotlin.Float",
    "double" to "kotlin.Double",
    "boolean" to "kotlin.Boolean",
    "java.lang.Object" to "kotlin.Any",
    "java.lang.Cloneable" to "kotlin.Cloneable",
    "java.lang.Comparable" to "kotlin.Comparable",
    "java.lang.Enum" to "kotlin.Enum",
    "java.lang.Annotation" to "kotlin.Annotation",
    "java.lang.CharSequence" to "kotlin.CharSequence",
    "java.lang.String" to "kotlin.String",
    "java.lang.Number" to "kotlin.Number",
    "java.lang.Throwable" to "kotlin.Throwable",
    "java.lang.Byte" to "kotlin.Byte",
    "java.lang.Short" to "kotlin.Short",
    "java.lang.Integer" to "kotlin.Int",
    "java.lang.Long" to "kotlin.Long",
    "java.lang.Character" to "kotlin.Char",
    "java.lang.Float" to "kotlin.Float",
    "java.lang.Double" to "kotlin.Double",
    "java.lang.Boolean" to "kotlin.Boolean",
    "java.lang.Iterable" to "kotlin.collections.Iterable",
    "java.util.List" to "kotlin.collections.MutableList",
    "java.util.Map" to "kotlin.collections.MutableMap"
).toMap()


val primitiveTypeNeutralLiterals = listOf(
    "byte" to "0.",
    "short" to "0",
    "int" to "0",
    "long" to "0",
    "char" to "kotlin.Char.MIN_VALUE",
    "float" to "0.0f",
    "double" to "0.0",
    "boolean" to "false",
).toMap()

val arrayTypeMappings = listOf(
    "byte" to "kotlin.ByteArray",
    "short" to "kotlin.ShortArray",
    "int" to "kotlin.IntArray",
    "long" to "kotlin.LongArray",
    "char" to "kotlin.CharArray",
    "float" to "kotlin.FloatArray",
    "double" to "kotlin.DoubleArray",
    "boolean" to "kotlin.BooleanArray",
    "java.lang.Byte" to "kotlin.ByteArray",
    "java.lang.Short" to "kotlin.ShortArray",
    "java.lang.Integer" to "kotlin.IntArray",
    "java.lang.Long" to "kotlin.LongArray",
    "java.lang.Character" to "kotlin.CharArray",
    "java.lang.Float" to "kotlin.FloatArray",
    "java.lang.Double" to "kotlin.DoubleArray",
    "java.lang.Boolean" to "kotlin.BooleanArray"
).toMap()
fun JType.toKotlin(): ClassTypeUse =
    when {
        isPrimitive -> ClassTypeUse(typeMappings[name()] ?: fullName())
        isArray -> ClassTypeUse(qn(arrayTypeMappings[this.elementType().name()] ?: "kotlin.Array")).apply {
            arg(
                elementType().toKotlin()
            )
        }
        isReference -> {
            val cls = this as JClass
            when {
                cls.isParameterized -> ClassTypeUse(
                    typeMappings[cls.erasure().fullName()] ?: cls.erasure().fullName()
                ).apply {
                    cls.typeParameters.forEach {
                        arg(it.toKotlin())
                    }
                }
                else -> ClassTypeUse(typeMappings[cls.fullName()] ?: cls.fullName())
            }
        }
        else -> KClassUse.nothing
    }

fun JAnnotationUse.toKotlin(isParameter: Boolean = false): AnnotationUse =
    AnnotationUse(annotationClass.toKotlin(), isParameter).apply {
        args {
            val count = annotationMembers.size
            annotationMembers.forEach { (name, annotationValue) ->
                when (annotationValue) {
                    is JAnnotationArrayMember ->
                        if (name == "value") {
                            annotationValue.annotations2().forEach { av ->
                                arg(handleValue(this@toKotlin, name, av))
                            }
                        } else {
                            arg(
                                v(name).assign(
                                    array {
                                        annotationValue.annotations2().forEach { av ->
                                            el(handleValue(this@toKotlin, name, av))
                                        }
                                    }
                                )
                            )
                        }

                    is JAnnotationClassValue -> namedArg(
                        name, count,
                        if (annotationValue.value() == null)
                            annotationValue.type().toKotlin().dotClass()
                        else
                            annotationValue.type().toKotlin().ref(v(annotationValue.value()))
                    )

                    is JAnnotationUse -> namedArg(
                        name, count,
                        annotationValue.toKotlin(true)
                    )

                    else ->
                        namedArg(name, count, lit(quoteAnnotationValue(name, annotationValue)))
                }
            }
        }
    }

fun ConstructorInv.namedArg(name: String, count: Int, e: Expression) =
    if (name == "value" && count == 1) arg(e) else arg(context.v(name).assign(e))

fun AnnotationUse.handleValue(au: JAnnotationUse, name: String, av: JAnnotationValue?) = when (av) {
    is JAnnotationUse -> av.toKotlin(true)
    is JAnnotationClassValue -> if (av.value() == null)
        av.type().toKotlin().dotClass()
    else
        av.type().toKotlin()
            .ref(v(av.value()))
    else -> lit(au.quoteAnnotationValue(name, av))
}

fun JAnnotationUse.quoteAnnotationValue(name: String, av: JAnnotationValue?): String =
    Class.forName(annotationClass.fullName())
        .methods
        .first {
            it.name == name
        }
        .returnType
        .let {
            if (java.lang.CharSequence::class.java.isAssignableFrom(it)
                || (it.isArray && java.lang.CharSequence::class.java.isAssignableFrom(it.componentType))
            ) "\"${av.toString()}\"" else av.toString()
        }

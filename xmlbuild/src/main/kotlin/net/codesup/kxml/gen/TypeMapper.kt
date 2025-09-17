package net.codesup.kxml.gen

import net.codesup.emit.QualifiedName
import net.codesup.emit.className
import net.codesup.emit.expressions.Expression
import net.codesup.emit.expressions.Variable
import net.codesup.emit.declaration.ParameterDeclaration
import net.codesup.emit.declaration.PropertyDeclaration
import net.codesup.emit.expressions.ExpressionContext
import java.math.BigDecimal
import java.math.BigInteger
import java.net.URI
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.MonthDay
import java.time.OffsetDateTime
import java.time.Year
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.io.encoding.Base64
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.staticFunctions

/**
 * @author Mirko Klemm 2025-09-11
 *
 */
typealias ToStringFunction = ExpressionContext.(ParameterDeclaration) -> Expression
const val XmlSchemaUri = "http://www.w3.org/2001/XMLSchema"

data class QName(val namespace: String, val name: String)
data class SimpleType(val qName: QName, val className: QualifiedName, val writeFunc: ToStringFunction)

fun <T:Any> findStandardParseFunction(kClass: KClass<T>): ExpressionContext.(PropertyDeclaration, Variable) -> Unit = (kClass.staticFunctions + (kClass.companionObject?.functions ?: emptyList())).firstOrNull {
    (it.name in setOf("of", "valueOf", "parse", "fromString", "from")
            || it.name.startsWith("parse"))
            && it.parameters.size == 1 && (it.parameters.first().type.classifier as? KClass<*>)?.isSubclassOf(CharSequence::class) == true
}?.let {
    { pd,v -> v(pd) assign call(it.name)(v) }
} ?: kClass.constructors.first { it.parameters.size == 1 && (it.parameters.first().type.classifier as KClass<*>).isSubclassOf(String::class) }.let {
    { pd,v -> v(pd) assign call(kClass)(v) }
}

val stringFormatter: ToStringFunction = { pd ->
    v(pd)
}

val defaultFormatter: ToStringFunction = { pd ->
    v(pd).fn("toString")
}

val valueFormatter: ToStringFunction = { pd ->
    v(pd).fn("value")
}

val enumFormatter: ToStringFunction = { pd ->
    v(pd).v("value")
}

val decimalFormatter: ToStringFunction = { pd ->
    v("xmlDecimalFormatter").fn("format", v(pd))
}

val base64Formatter: ToStringFunction = { pd ->
    t(Base64::class).fn("encode", v(pd))
}

val hexFormatter: ToStringFunction = { pd ->
    v(pd) ref call("toHexString")
}

fun createDateFormatFunction(formatName: String): ToStringFunction = { pd ->
    t(DateTimeFormatter::class).v(formatName).fn("format", v(pd))
}

private fun xsdType(name: String, kClass: KClass<*>, formatterDecl: ToStringFunction = defaultFormatter) = SimpleType(
    QName(XmlSchemaUri, name), className(kClass), formatterDecl
)

val default = xsdType("anySimpleType", Any::class, defaultFormatter)

val map = listOf(
    xsdType("anySimpleType", Any::class, defaultFormatter),
    xsdType("string", String::class, stringFormatter),
    xsdType("token", String::class, stringFormatter),
    xsdType("NMTOKEN", String::class, stringFormatter),
    xsdType("NCName", String::class, stringFormatter),
    xsdType("Name", String::class, stringFormatter),
    xsdType("ID", String::class, stringFormatter),
    xsdType("IDREF", String::class, stringFormatter),
    xsdType("string", String::class, stringFormatter),
    xsdType("boolean", Boolean::class, defaultFormatter),
    xsdType("integer", BigInteger::class, defaultFormatter),
    xsdType("int", BigInteger::class, defaultFormatter),
    xsdType("long", Long::class, defaultFormatter),
    xsdType("float", Float::class, decimalFormatter),
    xsdType("double", Double::class, decimalFormatter),
    xsdType("date", LocalDate::class, createDateFormatFunction("ISO_LOCAL_DATE")),
    xsdType("time", LocalDate::class, createDateFormatFunction("ISO_OFFSET_TIME")),
    xsdType("dateTime", LocalDateTime::class, defaultFormatter),
    xsdType("dateTimeStamp", OffsetDateTime::class, createDateFormatFunction("ISO_OFFSET_DATE_TIME")),
    xsdType("anyURI", URI::class, defaultFormatter),
    xsdType("base64Binary", ByteArray::class, base64Formatter),
    xsdType("hexBinary", ByteArray::class, hexFormatter),
    xsdType("byte", Byte::class, defaultFormatter),
    xsdType("short", Short::class, defaultFormatter),
    xsdType("decimal", BigDecimal::class, decimalFormatter),
    xsdType("positiveInteger", BigInteger::class, defaultFormatter),
    xsdType("nonPositiveInteger", BigInteger::class, defaultFormatter),
    xsdType("negativeInteger", BigInteger::class, defaultFormatter),
    xsdType("nonNegativeInteger", BigInteger::class, defaultFormatter),
    xsdType("unsignedByte", UByte::class, defaultFormatter),
    xsdType("unsignedShort", UShort::class, defaultFormatter),
    xsdType("unsignedInt", UInt::class, defaultFormatter),
    xsdType("unsignedLong", ULong::class, defaultFormatter),
    xsdType("duration", Duration::class, defaultFormatter),
    xsdType("dayTimeDuration", Duration::class, defaultFormatter),
    xsdType("yearMonthDuration", Duration::class, defaultFormatter),
    xsdType("gYearMonth", YearMonth::class, defaultFormatter),
    xsdType("gYear", Year::class, defaultFormatter),
    xsdType("gMonthDay", MonthDay::class, defaultFormatter),
    xsdType("gMonth", Month::class, defaultFormatter),
    xsdType("gDay", Int::class, defaultFormatter),
).associateBy {
    it.qName
}

class TypeMapper(definedSimpleTypes: List<SimpleType>) {
    val definedSimpleTypeMap: Map<QName, SimpleType> = definedSimpleTypes.associateBy { it.qName }
    fun mapSimpleType(namespace: String, xsdName: String): SimpleType = definedSimpleTypeMap[QName(namespace, xsdName)] ?: map[QName(namespace, xsdName)] ?: default
}

package net.codesup.kxml.gen

import java.util.*
import kotlin.reflect.KProperty

/**
 * @author Mirko Klemm 2025-09-17
 *
 */
class XmlBuilderSettings(properties: Properties) {
    constructor(resourceName: String = "XmlBuilderSettings.properties") : this(Properties().apply { load(XmlBuilderSettings::class.java.getResourceAsStream(resourceName)) })
    val generateAttributesAsProperties:Boolean by properties
}

inline operator fun Properties.getValue(thisRef: XmlBuilderSettings, property: KProperty<*>): Boolean {
    return this[property.name].toString().toBoolean()
}


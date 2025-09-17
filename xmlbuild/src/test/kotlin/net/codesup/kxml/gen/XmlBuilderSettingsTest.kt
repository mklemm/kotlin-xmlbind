package net.codesup.kxml.gen

import kotlin.test.Test

/**
 * @author Mirko Klemm 2025-09-17
 *
 */
class XmlBuilderSettingsTest {
    @Test
    fun testLoad() {
        assert(XmlBuilderSettings().generateAttributesAsProperties)
    }
}

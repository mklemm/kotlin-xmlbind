package net.codesup.kxml.gen

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.xml.sax.InputSource
import java.nio.file.Path

/**
 * @author Mirko Klemm 2025-09-10
 *
 */
class WriterGeneratorTest {


    @Test
    fun parse() {
        val writerGenerator = WriterGenerator(listOf(
            InputSource(
                WriterGeneratorTest::class.java.getResourceAsStream(
                    "/orm_3_2.xsd"
                )!!
            ).apply { systemId = "orm_3_2.xsd" }
        ), setOf("https://jakarta.ee/xml/ns/persistence/orm"))
        writerGenerator.generateBuilders(Path.of("src/test/kotlin"))
    }

    @Test
    fun createPackageName() {
        val pkgName1 = createPackageName("https://www.codesup.net/my/schema/1999/first")
        assertEquals("net.codesup.my.schema.`1999`.first", pkgName1.quotedStringValue)
        val pkgName2 = createPackageName("urn:my:schema:net")
        assertEquals("my.schema.net", pkgName2.quotedStringValue)
    }
    @Test
    fun createClassName() {
        val className = createClassName("my-HTML-class-name")
        assertEquals("MyHTMLClassName", className)

    }
    @Test
    fun createMemberName() {
        val memberName = createMemberName("HTML-source-file")
        assertEquals("htmlSourceFile", memberName)
    }

}

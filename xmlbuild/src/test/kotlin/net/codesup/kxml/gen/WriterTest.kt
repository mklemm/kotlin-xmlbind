package net.codesup.kxml.gen

import ee.jakarta.xml.ns.persistence.orm.entityMappings
import java.nio.file.Paths
import javax.xml.stream.XMLOutputFactory
import kotlin.io.path.outputStream
import kotlin.test.Test

/**
 * @author Mirko Klemm 2025-09-15
 *
 */
class WriterTest {
    @Test
    fun build() {
        Paths.get("build/tmp/test/orm.xml").outputStream().use { os ->
            val xmlWriter =
                XMLOutputFactory.newFactory().createXMLStreamWriter(os)
            entityMappings(xmlWriter) {
                version("3.2")
                entity {
                    `class`("net.codesup.kxml.gen.BuilderTest")
                    name("Test")
                    table {
                        name("TestTable")
                        uniqueConstraint {
                            name("TestUniqueConstraint")
                            columnName("ColumnA")
                        }
                    }
                }
            }
        }
    }
}

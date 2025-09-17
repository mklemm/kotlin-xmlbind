package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class PrimaryKeyJoinColumnBuilder(val xmlWriter: XMLStreamWriter) {

	fun referencedColumnName(referencedColumnName: String) {
		xmlWriter.writeAttribute("referenced-column-name", referencedColumnName)
	}

	fun columnDefinition(columnDefinition: String) {
		xmlWriter.writeAttribute("column-definition", columnDefinition)
	}

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun foreignKey(block: ForeignKeyBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "foreign-key")
		ForeignKeyBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

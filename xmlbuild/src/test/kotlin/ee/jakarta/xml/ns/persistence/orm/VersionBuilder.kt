package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class VersionBuilder(val xmlWriter: XMLStreamWriter) {

	fun access(access: AccessType) {
		xmlWriter.writeAttribute("access", access.value)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun column(block: ColumnBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "column")
		ColumnBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun temporal(temporal: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "temporal")
		xmlWriter.writeCharacters(temporal)
		xmlWriter.writeEndElement()
	}

}

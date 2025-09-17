package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class UniqueConstraintBuilder(val xmlWriter: XMLStreamWriter) {

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun columnName(columnName: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "column-name")
		xmlWriter.writeCharacters(columnName)
		xmlWriter.writeEndElement()
	}

}

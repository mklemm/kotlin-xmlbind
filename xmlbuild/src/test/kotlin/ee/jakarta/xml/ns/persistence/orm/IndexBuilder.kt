package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String

import javax.xml.stream.XMLStreamWriter

class IndexBuilder(val xmlWriter: XMLStreamWriter) {

	fun unique(unique: Boolean) {
		xmlWriter.writeAttribute("unique", unique.toString())
	}

	fun columnList(columnList: String) {
		xmlWriter.writeAttribute("column-list", columnList)
	}

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

}

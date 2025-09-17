package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class NamedQueryBuilder(val xmlWriter: XMLStreamWriter) {

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun query(query: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "query")
		xmlWriter.writeCharacters(query)
		xmlWriter.writeEndElement()
	}

	fun lockMode(lockMode: LockModeType) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "lock-mode")
		xmlWriter.writeCharacters(lockMode.value)
		xmlWriter.writeEndElement()
	}

	fun hint(block: QueryHintBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "hint")
		QueryHintBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

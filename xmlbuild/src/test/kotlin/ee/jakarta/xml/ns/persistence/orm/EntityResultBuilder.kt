package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class EntityResultBuilder(val xmlWriter: XMLStreamWriter) {

	fun entityClass(entityClass: String) {
		xmlWriter.writeAttribute("entity-class", entityClass)
	}

	fun discriminatorColumn(discriminatorColumn: String) {
		xmlWriter.writeAttribute("discriminator-column", discriminatorColumn)
	}

	fun lockMode(lockMode: LockModeType) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "lock-mode")
		xmlWriter.writeCharacters(lockMode.value)
		xmlWriter.writeEndElement()
	}

	fun fieldResult(block: FieldResultBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "field-result")
		FieldResultBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

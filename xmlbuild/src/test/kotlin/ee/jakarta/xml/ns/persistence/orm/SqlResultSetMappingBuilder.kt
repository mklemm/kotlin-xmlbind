package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class SqlResultSetMappingBuilder(val xmlWriter: XMLStreamWriter) {

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun entityResult(block: EntityResultBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "entity-result")
		EntityResultBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun constructorResult(block: ConstructorResultBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "constructor-result")
		ConstructorResultBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun columnResult(block: ColumnResultBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "column-result")
		ColumnResultBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

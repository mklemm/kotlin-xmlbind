package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class NamedNativeQueryBuilder(val xmlWriter: XMLStreamWriter) {

	fun resultClass(resultClass: String) {
		xmlWriter.writeAttribute("result-class", resultClass)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun resultSetMapping(resultSetMapping: String) {
		xmlWriter.writeAttribute("result-set-mapping", resultSetMapping)
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

	fun hint(block: QueryHintBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "hint")
		QueryHintBuilder(xmlWriter).apply(block)
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

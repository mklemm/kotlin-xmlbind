package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class NamedStoredProcedureQueryBuilder(val xmlWriter: XMLStreamWriter) {

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun procedureName(procedureName: String) {
		xmlWriter.writeAttribute("procedure-name", procedureName)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun parameter(block: StoredProcedureParameterBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "parameter")
		StoredProcedureParameterBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun resultClass(resultClass: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "result-class")
		xmlWriter.writeCharacters(resultClass)
		xmlWriter.writeEndElement()
	}

	fun resultSetMapping(resultSetMapping: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "result-set-mapping")
		xmlWriter.writeCharacters(resultSetMapping)
		xmlWriter.writeEndElement()
	}

	fun hint(block: QueryHintBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "hint")
		QueryHintBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

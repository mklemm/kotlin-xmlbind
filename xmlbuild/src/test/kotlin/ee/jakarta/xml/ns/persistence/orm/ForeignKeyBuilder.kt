package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class ForeignKeyBuilder(val xmlWriter: XMLStreamWriter) {

	fun constraintMode(constraintMode: ConstraintMode) {
		xmlWriter.writeAttribute("constraint-mode", constraintMode.value)
	}

	fun foreignKeyDefinition(foreignKeyDefinition: String) {
		xmlWriter.writeAttribute("foreign-key-definition", foreignKeyDefinition)
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

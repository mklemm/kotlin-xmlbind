package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class StoredProcedureParameterBuilder(val xmlWriter: XMLStreamWriter) {

	fun mode(mode: ParameterMode) {
		xmlWriter.writeAttribute("mode", mode.value)
	}

	fun `class`(`class`: String) {
		xmlWriter.writeAttribute("class", `class`)
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

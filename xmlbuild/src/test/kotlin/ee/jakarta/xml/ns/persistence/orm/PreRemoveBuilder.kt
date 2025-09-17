package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class PreRemoveBuilder(val xmlWriter: XMLStreamWriter) {

	fun methodName(methodName: String) {
		xmlWriter.writeAttribute("method-name", methodName)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

}

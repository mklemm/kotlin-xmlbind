package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String

import javax.xml.stream.XMLStreamWriter

class ConvertBuilder(val xmlWriter: XMLStreamWriter) {

	fun attributeName(attributeName: String) {
		xmlWriter.writeAttribute("attribute-name", attributeName)
	}

	fun disableConversion(disableConversion: Boolean) {
		xmlWriter.writeAttribute("disable-conversion", disableConversion.toString())
	}

	fun converter(converter: String) {
		xmlWriter.writeAttribute("converter", converter)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

}

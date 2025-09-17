package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String

import javax.xml.stream.XMLStreamWriter

class ConverterBuilder(val xmlWriter: XMLStreamWriter) {

	fun autoApply(autoApply: Boolean) {
		xmlWriter.writeAttribute("auto-apply", autoApply.toString())
	}

	fun `class`(`class`: String) {
		xmlWriter.writeAttribute("class", `class`)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

}

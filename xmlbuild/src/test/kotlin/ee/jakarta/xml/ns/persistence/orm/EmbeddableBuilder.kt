package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class EmbeddableBuilder(val xmlWriter: XMLStreamWriter) {

	fun access(access: AccessType) {
		xmlWriter.writeAttribute("access", access.value)
	}

	fun `class`(`class`: String) {
		xmlWriter.writeAttribute("class", `class`)
	}

	fun metadataComplete(metadataComplete: Boolean) {
		xmlWriter.writeAttribute("metadata-complete", metadataComplete.toString())
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun attributes(block: EmbeddableAttributesBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "attributes")
		EmbeddableAttributesBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

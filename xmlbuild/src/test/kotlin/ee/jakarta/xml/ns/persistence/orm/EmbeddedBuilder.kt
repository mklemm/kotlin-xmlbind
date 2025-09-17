package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class EmbeddedBuilder(val xmlWriter: XMLStreamWriter) {

	fun access(access: AccessType) {
		xmlWriter.writeAttribute("access", access.value)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun attributeOverride(block: AttributeOverrideBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "attribute-override")
		AttributeOverrideBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun associationOverride(block: AssociationOverrideBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "association-override")
		AssociationOverrideBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun convert(block: ConvertBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "convert")
		ConvertBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

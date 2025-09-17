package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class PersistenceUnitMetadataBuilder(val xmlWriter: XMLStreamWriter) {

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun xmlMappingMetadataComplete(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "xml-mapping-metadata-complete")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun persistenceUnitDefaults(block: PersistenceUnitDefaultsBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "persistence-unit-defaults")
		PersistenceUnitDefaultsBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

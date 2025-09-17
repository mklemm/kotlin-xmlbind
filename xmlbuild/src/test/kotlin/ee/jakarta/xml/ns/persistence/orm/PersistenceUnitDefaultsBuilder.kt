package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class PersistenceUnitDefaultsBuilder(val xmlWriter: XMLStreamWriter) {

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun schema(schema: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "schema")
		xmlWriter.writeCharacters(schema)
		xmlWriter.writeEndElement()
	}

	fun catalog(catalog: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "catalog")
		xmlWriter.writeCharacters(catalog)
		xmlWriter.writeEndElement()
	}

	fun delimitedIdentifiers(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "delimited-identifiers")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun access(access: AccessType) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "access")
		xmlWriter.writeCharacters(access.value)
		xmlWriter.writeEndElement()
	}

	fun cascadePersist(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade-persist")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun entityListeners(block: EntityListenersBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "entity-listeners")
		EntityListenersBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

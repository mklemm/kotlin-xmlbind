package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class EntityMappingsBuilder(val xmlWriter: XMLStreamWriter) {

	fun version(version: String) {
		xmlWriter.writeAttribute("version", version)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun persistenceUnitMetadata(block: PersistenceUnitMetadataBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "persistence-unit-metadata")
		PersistenceUnitMetadataBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun `package`(`package`: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "package")
		xmlWriter.writeCharacters(`package`)
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

	fun access(access: AccessType) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "access")
		xmlWriter.writeCharacters(access.value)
		xmlWriter.writeEndElement()
	}

	fun sequenceGenerator(block: SequenceGeneratorBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "sequence-generator")
		SequenceGeneratorBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun tableGenerator(block: TableGeneratorBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "table-generator")
		TableGeneratorBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun namedQuery(block: NamedQueryBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "named-query")
		NamedQueryBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun namedNativeQuery(block: NamedNativeQueryBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "named-native-query")
		NamedNativeQueryBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun namedStoredProcedureQuery(block: NamedStoredProcedureQueryBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "named-stored-procedure-query")
		NamedStoredProcedureQueryBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun sqlResultSetMapping(block: SqlResultSetMappingBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "sql-result-set-mapping")
		SqlResultSetMappingBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun mappedSuperclass(block: MappedSuperclassBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "mapped-superclass")
		MappedSuperclassBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun entity(block: EntityBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "entity")
		EntityBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun embeddable(block: EmbeddableBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "embeddable")
		EmbeddableBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun converter(block: ConverterBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "converter")
		ConverterBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

fun entityMappings(xmlWriter: XMLStreamWriter, block: EntityMappingsBuilder.() -> Unit) {
	xmlWriter.setDefaultNamespace("https://jakarta.ee/xml/ns/persistence/orm")
	xmlWriter.writeStartDocument()
	xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "entity-mappings")
	xmlWriter.writeDefaultNamespace("https://jakarta.ee/xml/ns/persistence/orm")
	EntityMappingsBuilder(xmlWriter).block()
	xmlWriter.writeEndElement()
	xmlWriter.writeEndDocument()
}

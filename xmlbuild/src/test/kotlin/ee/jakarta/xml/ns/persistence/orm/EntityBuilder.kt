package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class EntityBuilder(val xmlWriter: XMLStreamWriter) {

	fun cacheable(cacheable: Boolean) {
		xmlWriter.writeAttribute("cacheable", cacheable.toString())
	}

	fun access(access: AccessType) {
		xmlWriter.writeAttribute("access", access.value)
	}

	fun `class`(`class`: String) {
		xmlWriter.writeAttribute("class", `class`)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun metadataComplete(metadataComplete: Boolean) {
		xmlWriter.writeAttribute("metadata-complete", metadataComplete.toString())
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun table(block: TableBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "table")
		TableBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun secondaryTable(block: SecondaryTableBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "secondary-table")
		SecondaryTableBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun idClass(block: IdClassBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "id-class")
		IdClassBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun inheritance(block: InheritanceBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "inheritance")
		InheritanceBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun discriminatorValue(discriminatorValue: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "discriminator-value")
		xmlWriter.writeCharacters(discriminatorValue)
		xmlWriter.writeEndElement()
	}

	fun discriminatorColumn(block: DiscriminatorColumnBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "discriminator-column")
		DiscriminatorColumnBuilder(xmlWriter).apply(block)
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

	fun excludeDefaultListeners(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "exclude-default-listeners")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun excludeSuperclassListeners(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "exclude-superclass-listeners")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun entityListeners(block: EntityListenersBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "entity-listeners")
		EntityListenersBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun prePersist(block: PrePersistBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "pre-persist")
		PrePersistBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun postPersist(block: PostPersistBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "post-persist")
		PostPersistBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun preRemove(block: PreRemoveBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "pre-remove")
		PreRemoveBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun postRemove(block: PostRemoveBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "post-remove")
		PostRemoveBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun preUpdate(block: PreUpdateBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "pre-update")
		PreUpdateBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun postUpdate(block: PostUpdateBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "post-update")
		PostUpdateBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun postLoad(block: PostLoadBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "post-load")
		PostLoadBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
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

	fun namedEntityGraph(block: NamedEntityGraphBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "named-entity-graph")
		NamedEntityGraphBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun attributes(block: AttributesBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "attributes")
		AttributesBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

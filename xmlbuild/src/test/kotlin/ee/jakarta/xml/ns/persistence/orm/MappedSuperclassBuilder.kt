package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class MappedSuperclassBuilder(val xmlWriter: XMLStreamWriter) {

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

	fun idClass(block: IdClassBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "id-class")
		IdClassBuilder(xmlWriter).apply(block)
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

	fun attributes(block: AttributesBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "attributes")
		AttributesBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

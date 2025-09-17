package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class EntityListenerBuilder(val xmlWriter: XMLStreamWriter) {

	fun `class`(`class`: String) {
		xmlWriter.writeAttribute("class", `class`)
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
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

}

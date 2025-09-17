package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class AttributesBuilder(val xmlWriter: XMLStreamWriter) {

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun basic(block: BasicBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "basic")
		BasicBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun version(block: VersionBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "version")
		VersionBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun manyToOne(block: ManyToOneBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "many-to-one")
		ManyToOneBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun oneToMany(block: OneToManyBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "one-to-many")
		OneToManyBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun oneToOne(block: OneToOneBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "one-to-one")
		OneToOneBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun manyToMany(block: ManyToManyBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "many-to-many")
		ManyToManyBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun elementCollection(block: ElementCollectionBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "element-collection")
		ElementCollectionBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun embedded(block: EmbeddedBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "embedded")
		EmbeddedBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun transient(block: TransientBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "transient")
		TransientBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

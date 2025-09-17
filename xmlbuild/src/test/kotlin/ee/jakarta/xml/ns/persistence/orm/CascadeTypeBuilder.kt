package ee.jakarta.xml.ns.persistence.orm

import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class CascadeTypeBuilder(val xmlWriter: XMLStreamWriter) {

	fun cascadeAll(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade-all")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun cascadePersist(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade-persist")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun cascadeMerge(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade-merge")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun cascadeRemove(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade-remove")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun cascadeRefresh(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade-refresh")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun cascadeDetach(block: EmptyTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade-detach")
		EmptyTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

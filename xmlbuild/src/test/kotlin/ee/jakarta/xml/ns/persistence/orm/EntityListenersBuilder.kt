package ee.jakarta.xml.ns.persistence.orm

import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class EntityListenersBuilder(val xmlWriter: XMLStreamWriter) {

	fun entityListener(block: EntityListenerBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "entity-listener")
		EntityListenerBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

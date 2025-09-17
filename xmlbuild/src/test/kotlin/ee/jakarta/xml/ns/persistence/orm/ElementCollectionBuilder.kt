package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class ElementCollectionBuilder(val xmlWriter: XMLStreamWriter) {

	fun access(access: AccessType) {
		xmlWriter.writeAttribute("access", access.value)
	}

	fun fetch(fetch: FetchType) {
		xmlWriter.writeAttribute("fetch", fetch.value)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun targetClass(targetClass: String) {
		xmlWriter.writeAttribute("target-class", targetClass)
	}

	fun collectionTable(block: CollectionTableBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "collection-table")
		CollectionTableBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

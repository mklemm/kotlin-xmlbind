package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class ManyToManyBuilder(val xmlWriter: XMLStreamWriter) {

	fun targetEntity(targetEntity: String) {
		xmlWriter.writeAttribute("target-entity", targetEntity)
	}

	fun access(access: AccessType) {
		xmlWriter.writeAttribute("access", access.value)
	}

	fun fetch(fetch: FetchType) {
		xmlWriter.writeAttribute("fetch", fetch.value)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun mappedBy(mappedBy: String) {
		xmlWriter.writeAttribute("mapped-by", mappedBy)
	}

	fun joinTable(block: JoinTableBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "join-table")
		JoinTableBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun cascade(block: CascadeTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade")
		CascadeTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

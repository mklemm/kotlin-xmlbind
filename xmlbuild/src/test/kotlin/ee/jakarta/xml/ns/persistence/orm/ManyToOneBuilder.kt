package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class ManyToOneBuilder(val xmlWriter: XMLStreamWriter) {

	fun mapsId(mapsId: String) {
		xmlWriter.writeAttribute("maps-id", mapsId)
	}

	fun optional(optional: Boolean) {
		xmlWriter.writeAttribute("optional", optional.toString())
	}

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

	fun id(id: Boolean) {
		xmlWriter.writeAttribute("id", id.toString())
	}

	fun cascade(block: CascadeTypeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "cascade")
		CascadeTypeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class BasicBuilder(val xmlWriter: XMLStreamWriter) {

	fun optional(optional: Boolean) {
		xmlWriter.writeAttribute("optional", optional.toString())
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

	fun column(block: ColumnBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "column")
		ColumnBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

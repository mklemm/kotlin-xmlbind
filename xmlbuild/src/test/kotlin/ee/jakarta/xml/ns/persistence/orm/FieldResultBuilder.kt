package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class FieldResultBuilder(val xmlWriter: XMLStreamWriter) {

	fun column(column: String) {
		xmlWriter.writeAttribute("column", column)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

}

package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String

import javax.xml.stream.XMLStreamWriter

class OrderColumnBuilder(val xmlWriter: XMLStreamWriter) {

	fun insertable(insertable: Boolean) {
		xmlWriter.writeAttribute("insertable", insertable.toString())
	}

	fun nullable(nullable: Boolean) {
		xmlWriter.writeAttribute("nullable", nullable.toString())
	}

	fun updatable(updatable: Boolean) {
		xmlWriter.writeAttribute("updatable", updatable.toString())
	}

	fun columnDefinition(columnDefinition: String) {
		xmlWriter.writeAttribute("column-definition", columnDefinition)
	}

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

}

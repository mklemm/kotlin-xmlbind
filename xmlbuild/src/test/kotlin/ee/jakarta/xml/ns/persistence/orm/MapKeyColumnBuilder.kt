package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String

import java.math.BigInteger
import javax.xml.stream.XMLStreamWriter

class MapKeyColumnBuilder(val xmlWriter: XMLStreamWriter) {

	fun length(length: BigInteger) {
		xmlWriter.writeAttribute("length", length.toString())
	}

	fun insertable(insertable: Boolean) {
		xmlWriter.writeAttribute("insertable", insertable.toString())
	}

	fun table(table: String) {
		xmlWriter.writeAttribute("table", table)
	}

	fun scale(scale: BigInteger) {
		xmlWriter.writeAttribute("scale", scale.toString())
	}

	fun precision(precision: BigInteger) {
		xmlWriter.writeAttribute("precision", precision.toString())
	}

	fun nullable(nullable: Boolean) {
		xmlWriter.writeAttribute("nullable", nullable.toString())
	}

	fun unique(unique: Boolean) {
		xmlWriter.writeAttribute("unique", unique.toString())
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

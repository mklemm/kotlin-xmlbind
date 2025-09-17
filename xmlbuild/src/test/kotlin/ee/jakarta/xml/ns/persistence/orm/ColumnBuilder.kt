package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String
import kotlin.Unit

import java.math.BigInteger
import javax.xml.stream.XMLStreamWriter

class ColumnBuilder(val xmlWriter: XMLStreamWriter) {

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

	fun secondPrecision(secondPrecision: BigInteger) {
		xmlWriter.writeAttribute("second-precision", secondPrecision.toString())
	}

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun comment(comment: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "comment")
		xmlWriter.writeCharacters(comment)
		xmlWriter.writeEndElement()
	}

	fun checkConstraint(block: CheckConstraintBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "check-constraint")
		CheckConstraintBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

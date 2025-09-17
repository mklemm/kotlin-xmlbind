package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import java.math.BigInteger
import javax.xml.stream.XMLStreamWriter

class TableGeneratorBuilder(val xmlWriter: XMLStreamWriter) {

	fun schema(schema: String) {
		xmlWriter.writeAttribute("schema", schema)
	}

	fun initialValue(initialValue: BigInteger) {
		xmlWriter.writeAttribute("initial-value", initialValue.toString())
	}

	fun table(table: String) {
		xmlWriter.writeAttribute("table", table)
	}

	fun catalog(catalog: String) {
		xmlWriter.writeAttribute("catalog", catalog)
	}

	fun pkColumnName(pkColumnName: String) {
		xmlWriter.writeAttribute("pk-column-name", pkColumnName)
	}

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun valueColumnName(valueColumnName: String) {
		xmlWriter.writeAttribute("value-column-name", valueColumnName)
	}

	fun pkColumnValue(pkColumnValue: String) {
		xmlWriter.writeAttribute("pk-column-value", pkColumnValue)
	}

	fun allocationSize(allocationSize: BigInteger) {
		xmlWriter.writeAttribute("allocation-size", allocationSize.toString())
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

	fun uniqueConstraint(block: UniqueConstraintBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "unique-constraint")
		UniqueConstraintBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun index(block: IndexBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "index")
		IndexBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun checkConstraint(block: CheckConstraintBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "check-constraint")
		CheckConstraintBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

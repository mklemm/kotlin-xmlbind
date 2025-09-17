package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class MapKeyJoinColumnBuilder(val xmlWriter: XMLStreamWriter) {

	fun insertable(insertable: Boolean) {
		xmlWriter.writeAttribute("insertable", insertable.toString())
	}

	fun table(table: String) {
		xmlWriter.writeAttribute("table", table)
	}

	fun referencedColumnName(referencedColumnName: String) {
		xmlWriter.writeAttribute("referenced-column-name", referencedColumnName)
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

	fun foreignKey(block: ForeignKeyBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "foreign-key")
		ForeignKeyBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class TableBuilder(val xmlWriter: XMLStreamWriter) {

	fun schema(schema: String) {
		xmlWriter.writeAttribute("schema", schema)
	}

	fun catalog(catalog: String) {
		xmlWriter.writeAttribute("catalog", catalog)
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

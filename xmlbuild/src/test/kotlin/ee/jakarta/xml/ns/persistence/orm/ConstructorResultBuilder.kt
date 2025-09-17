package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class ConstructorResultBuilder(val xmlWriter: XMLStreamWriter) {

	fun targetClass(targetClass: String) {
		xmlWriter.writeAttribute("target-class", targetClass)
	}

	fun column(block: ColumnResultBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "column")
		ColumnResultBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

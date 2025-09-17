package ee.jakarta.xml.ns.persistence.orm

import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class NamedSubgraphBuilder(val xmlWriter: XMLStreamWriter) {

	fun `class`(`class`: String) {
		xmlWriter.writeAttribute("class", `class`)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun namedAttributeNode(block: NamedAttributeNodeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "named-attribute-node")
		NamedAttributeNodeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

package ee.jakarta.xml.ns.persistence.orm

import kotlin.Boolean
import kotlin.String
import kotlin.Unit

import javax.xml.stream.XMLStreamWriter

class NamedEntityGraphBuilder(val xmlWriter: XMLStreamWriter) {

	fun includeAllAttributes(includeAllAttributes: Boolean) {
		xmlWriter.writeAttribute("include-all-attributes", includeAllAttributes.toString())
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun namedAttributeNode(block: NamedAttributeNodeBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "named-attribute-node")
		NamedAttributeNodeBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun subgraph(block: NamedSubgraphBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "subgraph")
		NamedSubgraphBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

	fun subclassSubgraph(block: NamedSubgraphBuilder.() -> Unit) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "subclass-subgraph")
		NamedSubgraphBuilder(xmlWriter).apply(block)
		xmlWriter.writeEndElement()
	}

}

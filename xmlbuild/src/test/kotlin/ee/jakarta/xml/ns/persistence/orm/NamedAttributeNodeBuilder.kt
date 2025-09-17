package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class NamedAttributeNodeBuilder(val xmlWriter: XMLStreamWriter) {

	fun subgraph(subgraph: String) {
		xmlWriter.writeAttribute("subgraph", subgraph)
	}

	fun keySubgraph(keySubgraph: String) {
		xmlWriter.writeAttribute("key-subgraph", keySubgraph)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

}

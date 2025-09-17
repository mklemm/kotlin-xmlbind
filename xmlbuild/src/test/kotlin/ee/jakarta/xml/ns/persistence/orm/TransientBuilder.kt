package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class TransientBuilder(val xmlWriter: XMLStreamWriter) {

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

}

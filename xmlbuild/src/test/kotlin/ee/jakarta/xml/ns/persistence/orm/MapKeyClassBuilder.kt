package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class MapKeyClassBuilder(val xmlWriter: XMLStreamWriter) {

	fun `class`(`class`: String) {
		xmlWriter.writeAttribute("class", `class`)
	}

}

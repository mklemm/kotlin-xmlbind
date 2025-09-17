package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class IdClassBuilder(val xmlWriter: XMLStreamWriter) {

	fun `class`(`class`: String) {
		xmlWriter.writeAttribute("class", `class`)
	}

}

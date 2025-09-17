package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class CheckConstraintBuilder(val xmlWriter: XMLStreamWriter) {

	fun constraint(constraint: String) {
		xmlWriter.writeAttribute("constraint", constraint)
	}

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

}

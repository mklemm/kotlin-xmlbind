package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import java.math.BigInteger
import javax.xml.stream.XMLStreamWriter

class SequenceGeneratorBuilder(val xmlWriter: XMLStreamWriter) {

	fun schema(schema: String) {
		xmlWriter.writeAttribute("schema", schema)
	}

	fun initialValue(initialValue: BigInteger) {
		xmlWriter.writeAttribute("initial-value", initialValue.toString())
	}

	fun catalog(catalog: String) {
		xmlWriter.writeAttribute("catalog", catalog)
	}

	fun sequenceName(sequenceName: String) {
		xmlWriter.writeAttribute("sequence-name", sequenceName)
	}

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

	fun allocationSize(allocationSize: BigInteger) {
		xmlWriter.writeAttribute("allocation-size", allocationSize.toString())
	}

	fun description(description: String) {
		xmlWriter.writeStartElement("https://jakarta.ee/xml/ns/persistence/orm", "description")
		xmlWriter.writeCharacters(description)
		xmlWriter.writeEndElement()
	}

}

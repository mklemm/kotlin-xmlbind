package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import java.math.BigInteger
import javax.xml.stream.XMLStreamWriter

class DiscriminatorColumnBuilder(val xmlWriter: XMLStreamWriter) {

	fun length(length: BigInteger) {
		xmlWriter.writeAttribute("length", length.toString())
	}

	fun discriminatorType(discriminatorType: DiscriminatorType) {
		xmlWriter.writeAttribute("discriminator-type", discriminatorType.value)
	}

	fun columnDefinition(columnDefinition: String) {
		xmlWriter.writeAttribute("column-definition", columnDefinition)
	}

	fun options(options: String) {
		xmlWriter.writeAttribute("options", options)
	}

	fun name(name: String) {
		xmlWriter.writeAttribute("name", name)
	}

}

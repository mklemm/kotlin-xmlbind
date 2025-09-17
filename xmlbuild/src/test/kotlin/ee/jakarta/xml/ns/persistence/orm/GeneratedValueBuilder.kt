package ee.jakarta.xml.ns.persistence.orm

import kotlin.String

import javax.xml.stream.XMLStreamWriter

class GeneratedValueBuilder(val xmlWriter: XMLStreamWriter) {

	fun strategy(strategy: GenerationType) {
		xmlWriter.writeAttribute("strategy", strategy.value)
	}

	fun generator(generator: String) {
		xmlWriter.writeAttribute("generator", generator)
	}

}
